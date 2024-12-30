package com.example.myapplication_test.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.ExifInterface
import android.net.Uri
import android.util.Base64
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import java.io.ByteArrayOutputStream

@Composable
fun getLocalImage (): Pair<Uri?, ManagedActivityResultLauncher<String, Uri?>> {
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        imageUri = uri
    }
    return Pair(imageUri,launcher)
}

fun getImageFormatFromUri(context: Context, uri: Uri): Bitmap.CompressFormat {
    val mimeType = context.contentResolver.getType(uri)
    return when (mimeType) {
        "image/jpeg" -> Bitmap.CompressFormat.JPEG
        "image/webp" -> Bitmap.CompressFormat.WEBP
        else -> Bitmap.CompressFormat.PNG // 기본값
    }
}

fun getCorrectlyOrientedBitmap(context: Context, uri: Uri): Bitmap? {
    val inputStream = context.contentResolver.openInputStream(uri) ?: return null
    val bitmap = BitmapFactory.decodeStream(inputStream)
    inputStream.close()

    // EXIF 정보 읽기
    val exifInputStream = context.contentResolver.openInputStream(uri) ?: return bitmap
    val exif = ExifInterface(exifInputStream)
    val orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL)
    exifInputStream.close()

    // 회전 각도 계산
    val rotationDegrees = when (orientation) {
        ExifInterface.ORIENTATION_ROTATE_90 -> 90
        ExifInterface.ORIENTATION_ROTATE_180 -> 180
        ExifInterface.ORIENTATION_ROTATE_270 -> 270
        else -> 0
    }

    // 회전 각도가 0이면 원본 반환
    if (rotationDegrees == 0) {
        return bitmap
    }

    // 비트맵 회전
    val matrix = Matrix().apply { postRotate(rotationDegrees.toFloat()) }
    return Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
}

fun handleBitmapToBase64(context: Context, uri: Uri): String {
    val bitmap = getCorrectlyOrientedBitmap(context, uri) ?: return ""
    val format = getImageFormatFromUri(context, uri) // 포맷 판별
    return bitmapToBase64(bitmap, format) // 적절한 포맷으로 Base64 변환
}

// Bitmap 이미지를 Base64 문자열로 변환하는 함수
fun bitmapToBase64(bitmap: Bitmap, format: Bitmap.CompressFormat = Bitmap.CompressFormat.PNG): String {
    val outputStream = ByteArrayOutputStream()
    bitmap.compress(format, 30, outputStream) // 지정된 포맷으로 압축
    val byteArray = outputStream.toByteArray()
    return Base64.encodeToString(byteArray, Base64.DEFAULT)
}

// Base64 문자열을 Bitmap으로 변환하는 함수
fun base64ToBitmap(base64String: String): Bitmap {
    val decodedBytes = Base64.decode(base64String, Base64.DEFAULT) // Base64 문자열을 바이트 배열로 디코딩
    return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size) // 디코딩된 바이트 배열을 Bitmap으로 변환하여 반환
}

// JSON에서 Base64 문자열을 읽어 Bitmap으로 변환하는 함수
fun decodeImageFromJsonString(text: String): Bitmap {
    return base64ToBitmap(text)
}

// Bitmap 이미지를 Base64 문자열로 변환하고 JSON 문자열로 생성하는 함수
fun createJsonStringFromImage(bitmap: Bitmap): String {
    return bitmapToBase64(bitmap)
}
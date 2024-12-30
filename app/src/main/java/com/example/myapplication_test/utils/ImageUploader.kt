package com.example.myapplication_test.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import java.io.File

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

fun copyUriToInternalStorage(context: Context, uri: Uri, fileName: String): String {
    val inputStream = context.contentResolver.openInputStream(uri) ?: return ""
    val file = File(context.filesDir, fileName)
    val outputStream = file.outputStream()

    inputStream.use { input ->
        outputStream.use { output ->
            input.copyTo(output)
        }
    }
    return file.absolutePath // 복사된 파일 경로 반환
}

fun saveDrawableToInternalStorage(context: Context, drawableId: Int, fileName: String): String {
    // Drawable 리소스를 Bitmap으로 변환
    val drawable = context.resources.getDrawable(drawableId, context.theme)
    val bitmap = (drawable as BitmapDrawable).bitmap

    // 파일 경로 지정
    val file = File(context.filesDir, fileName)

    // Bitmap을 파일로 저장
    file.outputStream().use { outputStream ->
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream) // PNG로 저장
    }

    return file.absolutePath // 저장된 파일 경로 반환
}
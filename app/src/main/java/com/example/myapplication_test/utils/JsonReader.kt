package com.example.myapplication_test.utils

import android.content.Context
import com.example.myapplication_test.UserData
import kotlinx.serialization.json.Json


// fileName에 있는 json파일 읽어서 파싱해주는 함수
inline fun <reified T>parseJson(context: Context, fileName: String): List<T>{
    val jsonString = context.readJsonFile(fileName)
    return Json.decodeFromString(jsonString)
}
fun loadJson(context: Context, fileName: String): List<UserData> {
    return try {
        val jsonString = context.openFileInput(fileName).bufferedReader().use { it.readText() }
        Json.decodeFromString(jsonString)
    } catch (e: Exception) {
        emptyList() // 파일이 없거나 오류 발생 시 빈 리스트 반환
    }
}

// local file 읽어서 string으로 리턴해주는 함수
fun Context.readJsonFile(fileName: String): String {
    return assets.open(fileName).bufferedReader().use { it.readText() }
}
package com.example.myapplication_test.utils

import android.content.Context
import com.example.myapplication_test.UserData
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json


// fileName에 있는 json파일 읽어서 파싱해주는 함수
inline fun <reified T>parseJson(context: Context, fileName: String): List<T>{
    val jsonString = context.readJsonFile(fileName)
    return Json.decodeFromString(jsonString)
}
inline fun <reified T>loadJson(context: Context, fileName: String): MutableList<T> {
    return try {
        val jsonString = context.openFileInput(fileName).bufferedReader().use { it.readText() }
        Json.decodeFromString(jsonString)
    } catch (e: Exception) {
        mutableListOf()
    }
}

// local file 읽어서 string으로 리턴해주는 함수
fun Context.readJsonFile(fileName: String): String {
    return assets.open(fileName).bufferedReader().use { it.readText() }
}

fun saveJson(context: Context, fileName: String, data: List<UserData>) {
    val jsonString = Json.encodeToString(data)
    context.openFileOutput(fileName, Context.MODE_PRIVATE).use {
        it.write(jsonString.toByteArray())
    }
}
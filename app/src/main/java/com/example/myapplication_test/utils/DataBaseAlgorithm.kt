package com.example.myapplication_test.utils

fun FindValue(list:MutableList<Int>,value:Int):Boolean{
    for(item in list){
        if(item == value) return true
    }
    return false
}

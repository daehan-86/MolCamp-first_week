package com.example.myapplication_test.utils

import com.example.myapplication_test.GlobalVariables

fun FindValue(list:MutableList<Int>,value:Int):Boolean{
    for(item in list){
        if(item == value) return true
    }
    return false
}

fun badgeReload(){
    val cnt = GlobalVariables.badgeList.size
    for(user in GlobalVariables.userList){
        user.badgeCount = MutableList(cnt){0}
    }
    for(review in GlobalVariables.reviewList){
        val userdata = GlobalVariables.userList[review.owner]
        val placedata = GlobalVariables.placeList[review.place]
        for(i in placedata.badges){
            userdata.badgeCount[i]+=review.recommend
        }
    }
}
package com.example.myapplication_test.ui

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.myapplication_test.GlobalVariables
import com.example.myapplication_test.R

@Composable
fun TabSection(context: Context, showID: Int) {
    var selectedTab by remember { mutableStateOf(0) }
    val tabs = listOf("내 후기", "여행 갈곳")
    if(showID==GlobalVariables.userID){
        Column(modifier = Modifier.fillMaxSize()) {
            // Tab Buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                tabs.forEachIndexed { index, title ->
                    Text(
                        text = title,
                        modifier = Modifier
                            .clickable { selectedTab = index }
                            .padding(8.dp),
                        style = if (selectedTab == index) {
                            MaterialTheme.typography.bodyLarge
                        } else {
                            MaterialTheme.typography.bodyMedium
                        }
                    )
                }
            }

            // Tab Contents
            when (selectedTab) {
                0 -> RecyclerViewScreen(context, showID)
                1 -> SavePlace(context, showID)
            }
        }
    } else{
        RecyclerViewScreen(context, showID)
    }
}


@Composable
fun SavePlace(context: Context, showID:Int){
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(0.dp)
//            .verticalScroll(rememberScrollState()) // 스크롤 가능
    ) {
        GlobalVariables.userList[showID].myPlaceList.forEach { placeIter ->
            PlaceItem(placeIter = placeIter) // contactData 객체를 전달
        }
    }
}

@Composable
fun PlaceItem(placeIter:Int){
    var showDialog by remember { mutableStateOf(false) } // 다이얼로그 표시 여부 상태 관리
    val data = GlobalVariables.placeList[placeIter]
    var delete by remember { mutableStateOf(false) }
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp)
            .padding(bottom = 0.dp)
            .background(Color(0XFFFFFF)) // Box 배경색 설정
            .clickable { showDialog = true } // 클릭 시 다이얼로그 표시
    ) {
        // 박스에 제목 및 전화번호 표시
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(15.dp),
            verticalAlignment = Alignment.CenterVertically, // 세로 방향 중앙 정렬
            horizontalArrangement = Arrangement.SpaceBetween // 양쪽 끝으로 정렬
        ) {
            Image(
                painter = if(delete)painterResource(id = R.drawable.unsave_icon) else painterResource(id = R.drawable.save_icon), // 업로드한 이미지 파일 참조
                contentDescription = "Delete Icon",
                modifier = Modifier
                    .size(25.dp) // 크기 조정
                    .clickable{
                        delete = !delete
                        if(delete){
                            GlobalVariables.userList[GlobalVariables.userID].myPlaceList.remove(placeIter)
                        }
                        else{
                            GlobalVariables.userList[GlobalVariables.userID].myPlaceList.add(placeIter)
                        }
                    }
            )
            Row{
                Icon(
                    imageVector = Icons.Default.LocationOn,
                    contentDescription = "Location",
                    tint = Color.Black,
                    modifier = Modifier.size(28.dp) // 크기 조정
                )
                Text(
                    text = data.name,
                    color = if(delete)Color.Gray else Color.Black,
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }
    }
}
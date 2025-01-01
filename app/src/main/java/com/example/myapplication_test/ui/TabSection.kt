package com.example.myapplication_test.ui

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
    val tabs = listOf(R.drawable.tab1_img, R.drawable.tab2_img) // 이미지 리소스 ID 사용
    if (showID == GlobalVariables.userID) {
        Column(modifier = Modifier.fillMaxSize()) {
            // Tab Buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                tabs.forEachIndexed { index, imageRes ->
                    Image(
                        painter = painterResource(id = imageRes), // 이미지 리소스 사용
                        contentDescription = null, // 설명 텍스트
                        modifier = Modifier
                            .size(if (index == 1) 40.dp else 48.dp) // 두 번째 이미지 크기를 줄임
                            .clickable { selectedTab = index }
                            .padding(8.dp),
                        colorFilter = if (selectedTab == index) null else androidx.compose.ui.graphics.ColorFilter.tint(Color.Gray) // 선택 여부에 따라 색상 조정
                    )
                }
            }

            // Tab Contents
            when (selectedTab) {
                0 -> RecyclerViewScreen(context, showID)
                1 -> Text("여행 갈곳", modifier = Modifier.fillMaxSize())
            }
        }
    } else {
        RecyclerViewScreen(context, showID)
    }
}



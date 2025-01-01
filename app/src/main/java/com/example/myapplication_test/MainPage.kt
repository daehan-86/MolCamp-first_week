package com.example.myapplication_test

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.myapplication_test.page.HomeScreen
import com.example.myapplication_test.page.ReviewGrid
import com.example.myapplication_test.page.SettingsScreen
import com.example.myapplication_test.utils.pretendardFontFamily


@Composable
fun TabLayout(context: Context) {
    // 탭 상태 저장
    var selectedTabIndex by remember { mutableStateOf(0) }

    // 스크린별 UI 구성
    val screens = listOf("Info", "Image", "Profile")
    val icons = listOf(
        "ℹ️", // Info 아이콘 이모지
        "🖼️", // Image 아이콘 이모지
        "👤"  // Person 아이콘 이모지
    )

    Scaffold(
        bottomBar = { // 탭을 아래에 배치
            TabRow(
                selectedTabIndex = selectedTabIndex,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 0.dp, vertical = 8.dp),
                containerColor = Color(0xFF57B1FF), // 배경색 설정
                contentColor = Color.White // 텍스트 및 아이콘 색상
            ) {
                screens.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTabIndex == index,
                        onClick = { selectedTabIndex = index },
                        text = {
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = icons[index], // 이모지 표시
                                    modifier = Modifier.padding(end = 8.dp)
                                )
                                Text(title,
                                    fontFamily = pretendardFontFamily,
                                    fontWeight = FontWeight.SemiBold)
                            }
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // 탭에 따라 화면 변경
            when (selectedTabIndex) {
                0 -> HomeScreen()
                1 -> ReviewGrid(context)
                2 -> SettingsScreen(context, GlobalVariables.userID, {})
            }
        }
    }
}

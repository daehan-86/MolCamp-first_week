package com.example.myapplication_test

import android.content.Context
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.example.myapplication_test.page.HomeScreen
import com.example.myapplication_test.page.ReviewGrid
import com.example.myapplication_test.page.SettingsScreen


@Composable
fun TabLayout(context: Context) {
    // 탭 상태 저장
    var selectedTabIndex by remember { mutableStateOf(0) }

    // 스크린별 UI 구성
    val screens = listOf("Info", "Image", "Profile")

    Scaffold(
        bottomBar = { // 탭을 아래에 배치
            TabRow(
                selectedTabIndex = selectedTabIndex,
                modifier = Modifier.fillMaxWidth()
            ) {
                screens.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTabIndex == index,
                        onClick = { selectedTabIndex = index },
                        text = { Text(title) }
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
                2 -> SettingsScreen()
            }
        }
    }
}
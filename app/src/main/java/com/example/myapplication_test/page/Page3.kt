package com.example.myapplication_test.page

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp



// 간단한 설정 화면
@Composable
fun SettingsScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()) // 스크롤 추가
    ) {
        // 1. 상단 프로필 영역
        ProfileHeader()

        // 2. 하이라이트 영역
        BadgeSection()

        // 3. 탭 영역
        TabSection()
    }
}

// 상단 프로필 영역
@Composable
fun ProfileHeader() {
    var showDialog by remember { mutableStateOf(false) } // 다이얼로그 상태 변수

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        // 상단: 프로필 정보
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        ) {
            // 왼쪽: 프로필 이미지와 이름
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.weight(1f)
            ) {
                Box(
                    modifier = Modifier
                        .size(100.dp)
                        .background(Color.Gray, shape = CircleShape)
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "이서진",
                    style = MaterialTheme.typography.titleMedium
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            // 오른쪽: 2x2 매트릭스
            Column(
                modifier = Modifier
                    .weight(2f)
                    .fillMaxHeight(),
                verticalArrangement = Arrangement.SpaceEvenly
            ) {
                // 첫 번째 행
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "대한민국",
                            style = MaterialTheme.typography.bodyLarge,
                            modifier = Modifier.padding(bottom = 4.dp) // 독립적 간격 조정
                        )
                        Text(
                            text = "국적",
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "402",
                            style = MaterialTheme.typography.bodyLarge,
                            modifier = Modifier.padding(bottom = 4.dp) // 독립적 간격 조정
                        )
                        Text(
                            text = "팔로워",
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }

                Spacer(modifier = Modifier.height(30.dp))

                // 두 번째 행
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "497",
                            style = MaterialTheme.typography.bodyLarge,
                            modifier = Modifier.padding(bottom = 4.dp) // 독립적 간격 조정
                        )
                        Text(
                            text = "팔로잉",
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "1500",
                            style = MaterialTheme.typography.bodyLarge,
                            modifier = Modifier.padding(bottom = 4.dp) // 독립적 간격 조정
                        )
                        Text(
                            text = "좋아요",
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        // 중앙 아래: 프로필 편집 버튼
        Box(
            modifier = Modifier
                .fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            Button(onClick = { showDialog = true }) { // 다이얼로그 표시 상태를 true로 설정
                Text("프로필 편집")
            }
        }
    }

    // 다이얼로그 UI
    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false }, // 다이얼로그 외부 클릭 시 닫힘
            title = { Text("프로필 편집") }, // 다이얼로그 제목
            text = {
                Column {
                    Text("프로필 정보를 수정하세요.")
                    Spacer(modifier = Modifier.height(16.dp))
                    // 예: 사용자 이름 입력 필드
                    OutlinedTextField(
                        value = "이서진",
                        onValueChange = { /* 이름 변경 로직 추가 */ },
                        label = { Text("이름") }
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    // 국적 변경
                    OutlinedTextField(
                        value = "대한민국",
                        onValueChange = { /* 국적 변경 로직 추가 */ },
                        label = { Text("국적") }
                    )
                }
            },
            confirmButton = {
                Button(onClick = {
                    // 저장 로직 추가
                    showDialog = false // 다이얼로그 닫기
                }) {
                    Text("저장")
                }
            },
            dismissButton = {
                Button(onClick = { showDialog = false }) {
                    Text("취소")
                }
            }
        )
    }
}


// 하이라이트 영역
@Composable
fun BadgeSection() {
    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(5) { index ->
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Box(
                    modifier = Modifier
                        .size(70.dp)
                        .background(Color.LightGray, shape = CircleShape)
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "뱃지",
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }

    Spacer(modifier = Modifier.height(16.dp))
}

// 탭 영역
@Composable
fun TabSection() {
    var selectedTab by remember { mutableStateOf(0) }
    val tabs = listOf("클릭 1", "클릭 2", "클릭 3")

    Column {
        // 탭 버튼
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
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

        // 탭 내용
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.LightGray),
            contentAlignment = Alignment.Center
        ) {
            when (selectedTab) {
                0 -> Text("화면 1")
                1 -> Text("화면 2")
                2 -> Text("화면 3")
            }
        }
    }
}
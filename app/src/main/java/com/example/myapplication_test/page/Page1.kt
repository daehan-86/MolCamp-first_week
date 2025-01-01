package com.example.myapplication_test.page

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication_test.ContactData
import com.example.myapplication_test.GlobalVariables
import com.example.myapplication_test.R
import com.example.myapplication_test.utils.pretendardFontFamily

// JSON 데이터를 기반으로 박스를 렌더링하는 화면
@Composable
fun HomeScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(0.dp)
            .verticalScroll(rememberScrollState()) // 스크롤 가능
    ) {
        GlobalVariables.contactList.forEach { contactData ->
            BoxWithDialog(contactData = contactData) // contactData 객체를 전달
        }
    }
}
@Composable
fun BoxWithDialog(contactData: ContactData) {
    val context = LocalContext.current // Context 가져오기
    var showDialog by remember { mutableStateOf(false) } // 다이얼로그 표시 여부 상태 관리

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
            Text(
                text = contactData.name,
                color = Color.Black,
                fontFamily = pretendardFontFamily,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )
            Image(
                painter = painterResource(id = R.drawable.info_img), // drawable의 이미지 리소스 사용
                contentDescription = "More info image",
                modifier = Modifier.size(24.dp) // 이미지 크기 설정
            )
        }
    }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false }, // 다이얼로그 외부를 클릭하면 닫힘
            title = null, // 타이틀 제거
            text = {
                Column(
                    modifier = Modifier
                        .background(Color(0xFF57B1FF)) // 전체 다이얼로그 배경색
                        .padding(16.dp)
                ) {
                    // 제목
                    Text(
                        text = contactData.name,
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        color = Color.White
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                    // 설명
                    Text(
                        text = contactData.text,
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        color = Color.White
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                    // 버튼 영역
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        // 웹 버튼
                        Button(
                            onClick = {
                                val intent = android.content.Intent(
                                    android.content.Intent.ACTION_VIEW,
                                    android.net.Uri.parse(contactData.website)
                                )
                                context.startActivity(intent)
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF90CAF9))
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically // 아이콘과 텍스트 세로 중앙 정렬
                            ) {
                                // 버튼 아이콘 대체 (문자열 기반 텍스트)
                                Text(
                                    text = "🌐", // 웹과 관련된 이모지 사용 (원한다면 수정 가능)
                                    modifier = Modifier.padding(end = 8.dp),
                                    style = MaterialTheme.typography.bodyMedium
                                )
                                // 버튼 텍스트
                                Text(
                                    text = "Web",
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            }
                        }

                        // 전화 버튼
                        Button(
                            onClick = {
                                val intent = android.content.Intent(
                                    android.content.Intent.ACTION_DIAL,
                                    android.net.Uri.parse("tel:${contactData.tel}")
                                )
                                context.startActivity(intent)
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF90CAF9))
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.Phone, contentDescription = "Tel")
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("Tel")
                            }
                        }
                    }
                }
            },
            confirmButton = { /* 생략 가능 */ },
            containerColor = Color(0xFF57B1FF) // 다이얼로그 기본 배경색
        )
    }
}
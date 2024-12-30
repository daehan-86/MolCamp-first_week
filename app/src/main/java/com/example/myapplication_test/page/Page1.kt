package com.example.myapplication_test.page

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.myapplication_test.ContactData
import com.example.myapplication_test.GlobalVariables

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
            .background(Color(0xFF43A047)) // Box 배경색 설정
            .clickable { showDialog = true } // 클릭 시 다이얼로그 표시
    ) {
        // 박스에 제목 및 전화번호 표시
        Column(modifier = Modifier.padding(15.dp)) {
            Text(
                text = contactData.name,
                color = Color.White,
                style = MaterialTheme.typography.bodyLarge
            )
            Text(
                text = "Tel: ${contactData.tel}",
                color = Color.White,
                style = MaterialTheme.typography.bodySmall
            )
        }
    }

    // 다이얼로그 UI
    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false }, // 다이얼로그 외부를 클릭하면 닫힘
            title = { Text(contactData.name) }, // 다이얼로그 제목
            text = {
                Column(
                    modifier = Modifier
                        .background(Color(0xFFC8E6C9)) // 다이얼로그 배경색 설정
                        .padding(16.dp)
                ) {
                    Text(contactData.text) // 다이얼로그 내용 표시
                    Spacer(modifier = Modifier.height(16.dp)) // 간격 추가
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        // 전화번호
                        Text(
                            text = "Tel: ${contactData.tel}",
                            modifier = Modifier.weight(1f),
                            style = MaterialTheme.typography.bodyMedium
                        )
                        // 전화 버튼
                        Button(onClick = {
                            // 다이얼 앱으로 연결
                            val intent = android.content.Intent(
                                android.content.Intent.ACTION_DIAL,
                                android.net.Uri.parse("tel:${contactData.tel}")
                            )
                            context.startActivity(intent) // 다이얼 앱 실행
                        }) {
                            Text("Call") // 버튼 텍스트
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp)) // 간격 추가
                    // 웹사이트 이동 버튼
                    Button(onClick = {
                        // Chrome 앱으로 웹사이트 열기
                        val intent = android.content.Intent(
                            android.content.Intent.ACTION_VIEW,
                            android.net.Uri.parse(contactData.website)
                        )
                        context.startActivity(intent) // 웹사이트 이동
                    }) {
                        Text("Visit Website") // 버튼 텍스트
                    }
                }
            },
            confirmButton = {
                Button(onClick = { showDialog = false }) { // 확인 버튼 클릭 시 닫기
                    Text("Close")
                }
            },
            containerColor = Color(0xFFC8E6C9) // 다이얼로그 배경색 설정
        )
    }
}
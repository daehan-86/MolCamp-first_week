package com.example.myapplication_test

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.myapplication_test.utils.pretendardFontFamily


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    onLoginSuccess: (Int) -> Unit,
    onSignUp: (String, String, String) -> Unit
) {
    var userid by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }
    var showSignUpDialog by remember { mutableStateOf(false) }

    // 추가: 이미지 크기 및 위쪽 여백 상태 변수
    val imageSize = remember { mutableStateOf(100.dp) }
    val imageTopPadding = remember { mutableStateOf(50.dp) }

    Box(
        modifier = Modifier
            .background(Color.White)
            .padding(16.dp),
        contentAlignment = Alignment.TopCenter
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // 이미지 위쪽 여백 추가
            Spacer(modifier = Modifier.height(imageTopPadding.value))

            // 이미지 크기 및 비율 수정
            Image(
                painter = painterResource(id = R.drawable.lululala_img),
                contentDescription = "한국 여행 홍보",
                modifier = Modifier
                    .size(imageSize.value), // 정사각형 크기 설정
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.height(24.dp))

            // 아이디 입력
            TextField(
                value = userid,
                onValueChange = { userid = it },
                label = { Text(text="아이디 입력",
                    fontFamily = pretendardFontFamily,
                    fontWeight = FontWeight.Normal) },
                singleLine = true,
                colors = TextFieldDefaults.textFieldColors(
                    unfocusedIndicatorColor = Color.Gray,
                    focusedIndicatorColor = MaterialTheme.colorScheme.primary,
                    containerColor = Color.White
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 24.dp)
            )

            // 비밀번호 입력
            TextField(
                value = password,
                onValueChange = { password = it },
                label = { Text(text = "비밀번호 입력",
                    fontFamily = pretendardFontFamily,
                    fontWeight = FontWeight.Normal) },
                singleLine = true,
                visualTransformation = PasswordVisualTransformation(),
                colors = TextFieldDefaults.textFieldColors(
                    unfocusedIndicatorColor = Color.Gray,
                    focusedIndicatorColor = MaterialTheme.colorScheme.primary,
                    containerColor = Color.White
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 24.dp)
            )

            if (errorMessage.isNotEmpty()) {
                Text(errorMessage, color = Color.Red, modifier = Modifier.padding(bottom = 16.dp))
            }

            // 로그인 버튼
            Button(
                onClick = {
                    val user = GlobalVariables.userList.find { it.userid == userid && it.password == password }
                    if (user != null) {
                        onLoginSuccess(user.id)
                    } else {
                        errorMessage = "아이디 또는 비밀번호가 올바르지 않습니다."
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF57B1FF)
                )
            ) {
                Text("로그인", fontFamily = pretendardFontFamily,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.White)
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                TextButton(onClick = { showSignUpDialog = true }) {
                    Text(
                        text = "처음 오셨나요?",
                        fontFamily = pretendardFontFamily,
                        fontWeight = FontWeight.Normal,
                        color = Color(0xFF1E88E5)
                    )
                }
                TextButton(onClick = { showSignUpDialog = true }) {
                    Text(
                        text = "회원 가입하기",
                        fontFamily = pretendardFontFamily,
                        fontWeight = FontWeight.Normal,
                        color = Color(0xFF1E88E5)
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // SNS 로그인
            Text(
                text = "SNS 계정으로 로그인",
                fontFamily = pretendardFontFamily,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(vertical = 16.dp)
            )
            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier.fillMaxWidth()
            ) {
                SNSLoginButton(
                    backgroundColor = Color(0xFF1877F2),
                    iconRes = R.drawable.facebook_img,
                    buttonSize = 50.dp
                )
                SNSLoginButton(
                    backgroundColor = Color(0xFFFFE812),
                    iconRes = R.drawable.kakao_img,
                    buttonSize = 50.dp
                )
                SNSLoginButton(
                    backgroundColor = Color(0xFF03C75A),
                    iconRes = R.drawable.naver_img,
                    buttonSize = 50.dp
                )
            }
        }

        if (showSignUpDialog) {
            SignUpDialog(
                onDismiss = { showSignUpDialog = false },
                onSignUp = { newUserid, newPassword, newNationality ->
                    onSignUp(newUserid, newPassword, newNationality)
                    showSignUpDialog = false
                }
            )
        }
    }
}




@Composable
fun SNSLoginButton(
    backgroundColor: Color,
    iconRes: Int,
    buttonSize: Dp // 버튼 크기를 외부에서 지정
) {
    Button(
        onClick = { /* SNS 로그인 처리 */ },
        modifier = Modifier
            .size(buttonSize), // 버튼의 크기를 설정
        colors = ButtonDefaults.buttonColors(containerColor = backgroundColor),
        contentPadding = PaddingValues(0.dp), // 패딩을 제거하여 아이콘이 버튼을 꽉 채우도록 설정
        shape = MaterialTheme.shapes.medium // 버튼 모양 조정 (옵션)
    ) {
        Icon(
            painter = painterResource(id = iconRes),
            contentDescription = null,
            tint = Color.Unspecified, // 아이콘 색상 그대로 유지
            modifier = Modifier.fillMaxSize() // 버튼 내부를 아이콘이 완전히 채우도록 설정
        )
    }
}




@Composable
fun SignUpDialog(
    onDismiss: () -> Unit,
    onSignUp: (String, String, String) -> Unit
) {
    var userid by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var nationality by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Sign Up") },
        text = {
            Column {
                OutlinedTextField(
                    value = userid,
                    onValueChange = { userid = it },
                    label = { Text("Username") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(16.dp))
                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("Password") },
                    singleLine = true,
                    visualTransformation = PasswordVisualTransformation(),
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(16.dp))
                OutlinedTextField(
                    value = nationality,
                    onValueChange = { nationality = it },
                    label = { Text("Nationality") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                if (errorMessage.isNotEmpty()) {
                    Text(errorMessage, color = Color.Red, modifier = Modifier.padding(top = 8.dp))
                }
            }
        },
        confirmButton = {
            Button(onClick = {
                if (userid.isNotEmpty() && password.isNotEmpty()) {
                    onSignUp(userid, password, nationality)
                } else {
                    errorMessage = "All fields are required"
                }
            }) {
                Text("Sign Up")
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}


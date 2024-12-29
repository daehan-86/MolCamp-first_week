package com.example.myapplication_test

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp


@Composable
fun LoginScreen(
    onLoginSuccess: (Int) -> Unit,
    onSignUp: (String, String, String) -> Unit
){
    var userid by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }
    var showSignUpDialog by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Login", style = MaterialTheme.typography.headlineMedium)

            Spacer(modifier = Modifier.height(32.dp))

            OutlinedTextField(
                value = userid,
                onValueChange = { userid = it },
                label = { Text("Username") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)
            )

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Password") },
                singleLine = true,
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)
            )

            if (errorMessage.isNotEmpty()) {
                Text(errorMessage, color = Color.Red, modifier = Modifier.padding(bottom = 16.dp))
            }

            Button(onClick = {
                // 로그인 검증
                val user = GlobalVariables.userList.find { it.userid == userid && it.password == password }
                if (user != null) {
                    onLoginSuccess(user.id) // 로그인 성공
                } else {

                    errorMessage = "$userid  $password" // 에러 메시지
                }
            }, modifier = Modifier.fillMaxWidth()) {
                Text("Login")
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = { showSignUpDialog = true },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Sign Up")
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

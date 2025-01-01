package com.example.myapplication_test.page

import android.content.Context
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.example.myapplication_test.GlobalVariables
import com.example.myapplication_test.ui.TabSection
import com.example.myapplication_test.utils.copyUriToInternalStorage
import com.example.myapplication_test.utils.getLocalImage
import com.example.myapplication_test.utils.saveJson
import java.io.File


// 간단한 설정 화면
@Composable
fun SettingsScreen(context: Context, showID: Int, onClose:() -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ){
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()) // 스크롤 추가
        ) {
            if(showID!=GlobalVariables.userID){
                Button(
                    onClick = onClose
                ) {
                    Text(text = "X")
                }
            }
            // 1. 상단 프로필 영역
            ProfileHeader(showID)

            // 2. 하이라이트 영역
            BadgeSection(showID)

            // 3. 탭 영역
            TabSection(context = context,showID)
        }
    }
}

// 상단 프로필 영역
@Composable
fun ProfileHeader(showID:Int) {
    var showDialog by remember { mutableStateOf(false) } // 다이얼로그 상태 변수
    val data = GlobalVariables.userList[showID]
    var reviewRecommendCnt = 0
    for(o in data.reviews){
        reviewRecommendCnt+=GlobalVariables.reviewList[o].recommend
    }
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
            // 왼쪽: 프로필 이미지와 이름
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.weight(1f)
            ) {
                Image(
                    painter = // 이미지 전환 애니메이션
                    rememberAsyncImagePainter(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(data = Uri.fromFile(File(data.profile)))
                            .apply(block = fun ImageRequest.Builder.() {
                                crossfade(true) // 이미지 전환 애니메이션
                            })
                            .build()
                    ),
                    contentDescription = "Sample Image",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .width(100.dp)
                        .height(100.dp)
                        .clip(RoundedCornerShape(16.dp)) // 모서리 반경 설정
                        .border(3.dp, Color.White, RoundedCornerShape(16.dp)) // 정사각형 테두리 반경 설정
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = data.username,
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
                            text = data.nationality,
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
                            text = data.follower.size.toString(),
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
                            text = data.following.size.toString(),
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
                            text = reviewRecommendCnt.toString(),
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
        if (GlobalVariables.userID == showID) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly // 버튼 간격 조정
                ) {
                    Button(
                        onClick = { showDialog = true },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF57B1FF), // 배경색 설정
                            contentColor = Color.White // 텍스트 색상 설정
                        ),
                        modifier = Modifier.weight(1f) // 버튼 길이 동일하게 분배
                    ) {
                        Text("프로필 편집")
                    }
                    Spacer(modifier = Modifier.width(20.dp))
                    Button(
                        onClick = { GlobalVariables.userSession = false },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF57B1FF), // 배경색 설정
                            contentColor = Color.White // 텍스트 색상 설정
                        ),
                        modifier = Modifier.weight(1f) // 버튼 길이 동일하게 분배
                    ) {
                        Text("로그아웃")
                    }
                }
            }
        }


        else{
            var isFolowing by remember { mutableStateOf(data.follower.contains(GlobalVariables.userID)) }
            Box(
                modifier = Modifier
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                if(isFolowing){
                    // 이사람 팔로잉 중이라면
                    Button(
                        colors = ButtonDefaults.textButtonColors(
                            containerColor = Color.DarkGray, // 배경색 설정
                            contentColor = Color.White // 텍스트 색상
                        ),
                        onClick = {
                            data.follower.remove(GlobalVariables.userID)
                            GlobalVariables.userList[GlobalVariables.userID].following.remove(data.id)
                            isFolowing = false
                        }) { // 다이얼로그 표시 상태를 true로 설정
                        Text(text="팔로잉", color = Color.White)
                    }
                }
                else{
                    Button(
                        colors = ButtonDefaults.textButtonColors(
                            containerColor = Color.Blue, // 배경색 설정
                            contentColor = Color.White // 텍스트 색상
                        ),
                        onClick = {
                            data.follower.add(GlobalVariables.userID)
                            GlobalVariables.userList[GlobalVariables.userID].following.add(data.id)
                            isFolowing = true
                    }) { // 다이얼로그 표시 상태를 true로 설정
                        if(data.following.contains(GlobalVariables.userID)){
                            Text(text="맞팔로우", color = Color.White)
                        }
                        else{
                            Text(text="팔로우", color = Color.White)
                        }
                    }
                }
            }
        }
    }
    var tempname by remember { mutableStateOf(data.username) }
    var tempnation by remember { mutableStateOf(data.nationality) }
    val context = LocalContext.current // Context 가져오기
    // 다이얼로그 UI
    if (showDialog) {
        val (imageUri, launcher) = getLocalImage()

        AlertDialog(
            onDismissRequest = { showDialog = false }, // 다이얼로그 외부 클릭 시 닫힘
            title = { Text("프로필 편집") }, // 다이얼로그 제목
            text = {
                Column {
                    Box {
                        Button(
                            onClick = { launcher.launch("image/*") },
                            shape = RoundedCornerShape(0.dp), // 네모난 버튼
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFF57B1FF) // 버튼 색상 변경
                            ),
                            modifier = Modifier
                                .width(100.dp)
                                .height(100.dp)
                                .clip(CircleShape)
                        ) {
                            Text(
                                "+",
                                fontSize = 24.sp, // 텍스트 크기 증가
                                color = Color.White // 텍스트 색상 변경
                            )
                        }
                        imageUri?.let { uri ->
                            Image(
                                painter = rememberAsyncImagePainter(uri),
                                contentDescription = "Uploaded Image",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .width(100.dp)
                                    .height(100.dp)
                                    .clip(CircleShape)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    // 예: 사용자 이름 입력 필드
                    OutlinedTextField(
                        value = tempname,
                        onValueChange = { tempname = it },
                        label = { Text("이름") }
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    // 국적 변경
                    OutlinedTextField(
                        value = tempnation,
                        onValueChange = { tempnation = it },
                        label = { Text("국적") }
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        // 저장 로직 추가
                        showDialog = false // 다이얼로그 닫기
                        data.username = tempname
                        data.nationality = tempnation
                        imageUri?.let { uri ->
                            data.profile = copyUriToInternalStorage(context, uri, "profile${GlobalVariables.userID}.jpg")
                        }
                        saveJson(context = context, "users.json", GlobalVariables.userList)
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF57B1FF) // 버튼 색상 변경
                    )
                ) {
                    Text("저장", color = Color.White) // 텍스트 색상 변경
                }
            },
            dismissButton = {
                Button(
                    onClick = { showDialog = false },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF57B1FF) // 버튼 색상 변경
                    )
                ) {
                    Text("취소", color = Color.White) // 텍스트 색상 변경
                }
            }
        )
    } else {
        tempname = data.username
        tempnation = data.nationality
    }

}


// 하이라이트 영역
@Composable
fun BadgeSection(showID: Int) {
    val data = GlobalVariables.userList[showID].badgeCount
    val scrollState = rememberScrollState()
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(scrollState)
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        data.forEachIndexed  { index,item ->
            val name = GlobalVariables.badgeList[index].name
            if(item>=GlobalVariables.badgeList[index].gold) BadgeItem(image = GlobalVariables.badgeList[index].goldImageRoot, name = "${name}Ⅰ")
            else if(item>=GlobalVariables.badgeList[index].silver) BadgeItem(image = GlobalVariables.badgeList[index].silverImageRoot, name = "${name}Ⅱ")
            else if(item>=GlobalVariables.badgeList[index].bronze) BadgeItem(image = GlobalVariables.badgeList[index].bronzeImageRoot, name = "${name}Ⅲ")
        }
    }

    Spacer(modifier = Modifier.height(16.dp))
}
@Composable
fun BadgeItem(image:String,name:String){
    val context = LocalContext.current // Context 가져오기
    val resourceId = context.resources.getIdentifier(image, "drawable", context.packageName)
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Image(
            painter = painterResource(id = resourceId),
            contentDescription = "Image from $name",
            contentScale = ContentScale.Crop, // 이미지가 꽉 차도록 크롭
            modifier = Modifier
                .size(70.dp)
                .clip(CircleShape)
                .border(2.dp,Color.White, CircleShape)
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = name,
            style = MaterialTheme.typography.bodySmall
        )
    }
}



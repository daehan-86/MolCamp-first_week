package com.example.myapplication_test.page

import android.content.Context
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.example.myapplication_test.GlobalVariables
import com.example.myapplication_test.R
import com.example.myapplication_test.ReviewData
import com.example.myapplication_test.utils.copyUriToInternalStorage
import com.example.myapplication_test.utils.getLocalImage
import java.io.File


@Composable
fun ReviewGrid(context: Context) {
    var selectedLocation by remember { mutableStateOf<ReviewData?>(null) } // 선택된 이미지 상태
    var writeReviewMode by remember { mutableStateOf(false) }
    var imageReturnState by remember { mutableStateOf(false) }
    var showThisUser by remember { mutableIntStateOf(-1) }

    Box(modifier = Modifier.fillMaxSize()) {
        if (showThisUser != -1) {
            Dialog(
                onDismissRequest = { showThisUser = -1 }
            ) {
                SettingsScreen(context, showThisUser, { showThisUser = -1 })
            }
        } else if (writeReviewMode) {
            WriteReview(
                context,
                onClose = { writeReviewMode = false },
                onUpload = { ret ->
                    imageReturnState = true
                    GlobalVariables.userList[GlobalVariables.userID].reviews.add(ret.id)
                    GlobalVariables.reviewList.add(ret)
                }
            )
            if (imageReturnState) {
                writeReviewMode = false
                imageReturnState = false
            }
        } else if (selectedLocation == null) {
            // 기본 그리드
            LazyVerticalGrid(
                columns = GridCells.Fixed(2), // 2열 그리드
                modifier = Modifier.fillMaxSize(),
                horizontalArrangement = Arrangement.spacedBy(13.dp), // 사진 간 가로 간격
                verticalArrangement = Arrangement.spacedBy(13.dp), // 사진 간 세로 간격
                contentPadding = PaddingValues(13.dp) // 전체 그리드의 패딩
            ) {
                items(GlobalVariables.reviewList) { location ->
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(16.dp)) // Corner Radius 추가
                            .background(Color(0xFFE3F2FD)) // 배경색 추가
                            .aspectRatio(1f) // 정사각형 비율
                            .clickable { selectedLocation = location }
                    ) {
                        Image(
                            painter = rememberAsyncImagePainter(
                                model = Uri.fromFile(File(location.image))
                            ),
                            contentDescription = "Uploaded Image",
                            contentScale = ContentScale.Crop, // 이미지 크롭
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                }
            }

            // 업로드 버튼
            TextButton(
                onClick = { writeReviewMode = true },
                shape = RoundedCornerShape(200.dp), // 둥근 버튼
                colors = ButtonDefaults.textButtonColors(
                    containerColor = Color(0xFF90CAF9), // 버튼 배경색
                    contentColor = Color.White // 버튼 텍스트 색상
                ),
                modifier = Modifier
                    .size(100.dp) // 크기 설정
                    .padding(16.dp)
                    .align(Alignment.BottomEnd)
            ) {
                Text("+", color = Color.White, fontSize = 36.sp) // + 이모지
            }
        } else {
            // 확대된 이미지 뷰
            selectedLocation?.let { location ->
                ExpandedReview(
                    data = location,
                    onClose = { selectedLocation = null },
                    showUser = { showThisUser = location.owner }
                )
            }
        }
    }
}



@Composable
fun WriteReview(context: Context, onClose: () -> Unit, onUpload: (ReviewData) -> Unit) {
    val (imageUri, launcher) = getLocalImage()

    // 상태 저장
//    var locationText by remember { mutableStateOf("") } // 위치 입력용 상태
    var selectPlace by remember { mutableStateOf(0) }
    var reviewText by remember { mutableStateOf("") } // 후기 입력용 상태
    var satisfaction by remember { mutableStateOf(5) }
    var expanded by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFE3F2FD)) // 전체 배경색
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()) // 스크롤 가능
        ) {
            // 이미지 업로드 섹션
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1.5f) // 사진처럼 약간 직사각형 비율
                    .clip(RoundedCornerShape(16.dp)) // 모서리 둥글게
                    .background(Color.White) // 배경색
                    .clickable { launcher.launch("image/*") }
            ) {
                if (imageUri == null) {
                    Icon(
                        imageVector = Icons.Default.Add, // + 아이콘
                        contentDescription = "Upload Image",
                        modifier = Modifier
                            .align(Alignment.Center)
                            .size(60.dp),
                        tint = Color.Gray
                    )
                } else {
                    Image(
                        painter = rememberAsyncImagePainter(imageUri),
                        contentDescription = "Uploaded Image",
                        contentScale = ContentScale.Fit, // 원본 비율 유지
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 장소 입력 섹션
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp)) // 모서리 둥글게
                    .background(Color(0xFFBBDEFB))
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Place, // 장소 아이콘
                    contentDescription = "Location",
                    modifier = Modifier.size(24.dp),
                    tint = Color.Black
                )
                Spacer(modifier = Modifier.width(8.dp))
                Button(onClick = { expanded=true }) {
                    Text(text = GlobalVariables.placeList[selectPlace].name)
                }
                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    GlobalVariables.placeList.forEach { item ->
                        DropdownMenuItem(
                            onClick = {
                                selectPlace = item.id
                                expanded = false
                            },
                            text = {Text(text = item.name)}
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 만족도 슬라이더 섹션
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("만족도", style = MaterialTheme.typography.bodyLarge)
                Spacer(modifier = Modifier.width(8.dp))
                Slider(
                    value = satisfaction.toFloat(),
                    onValueChange = { satisfaction = it.toInt() },
                    valueRange = 1f..10f,
                    steps = 9,
                    modifier = Modifier.weight(1f) // 슬라이더를 남은 공간에 맞춤
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "$satisfaction",
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color.Black
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 후기 작성 텍스트 섹션
            OutlinedTextField(
                value = reviewText, // 후기 입력 상태 사용
                onValueChange = { reviewText = it },
                placeholder = { Text("후기 작성해주세요") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp),
                maxLines = 5,
                singleLine = false
            )

            Spacer(modifier = Modifier.height(16.dp))

            // 저장 버튼
            Button(
                onClick = {
                    imageUri?.let { uri ->
                        onUpload(
                            ReviewData(
                                id = GlobalVariables.reviewList.size,
                                owner = GlobalVariables.userID,
                                image = copyUriToInternalStorage(context, uri, "review${GlobalVariables.reviewList.size}.jpg"),
                                rating = satisfaction,
                                recommend = 0,
                                place = selectPlace, // 위치 입력값 전달
                                text = reviewText // 후기 입력값 전달
                            )
                        )
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .clip(RoundedCornerShape(16.dp)),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF90CAF9))
            ) {
                Text("업로드", color = Color.White, style = MaterialTheme.typography.bodyMedium)
            }
        }

        // 닫기 버튼
        IconButton(
            onClick = onClose,
            modifier = Modifier
                .size(40.dp)
                .align(Alignment.TopStart)
        ) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "Close",
                tint = Color.Black
            )
        }
    }
}

//@Composable
//fun ReviewItem(data: ReviewData, onItemClick: (ReviewData) -> Unit) {
//
//    Image(
//        painter = // 이미지 전환 애니메이션
//        rememberAsyncImagePainter(
//            ImageRequest.Builder(LocalContext.current).data(data = Uri.fromFile(File(data.image)))
//                .apply(block = fun ImageRequest.Builder.() {
//                    crossfade(true) // 이미지 전환 애니메이션
//                }).build()
//        ),
//        contentDescription = "Sample Image",
//        contentScale = ContentScale.Crop,
//        modifier = Modifier
//            .fillMaxWidth()
//            .aspectRatio(1f) // 정사각형
//            .clickable { onItemClick(data) } // 클릭 이벤트 전달
//    )
//}


@Composable
fun ExpandedReview(data: ReviewData, onClose: () -> Unit, showUser: () -> Unit) {
    val userdata = GlobalVariables.userList[GlobalVariables.userID]
    var isRecommend by remember { mutableStateOf(userdata.recommend.contains(data.id)) }
    var recommendCount by remember { mutableIntStateOf(data.recommend) }

    Dialog(
        onDismissRequest = { onClose() }
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFE3F2FD)) // 전체 배경색
                .padding(16.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally // 수평 정렬
            ) {
                Spacer(modifier = Modifier.height(32.dp)) // 이미지와 상단 버튼 간 간격 추가

                // 메인 이미지
                Image(
                    painter = rememberAsyncImagePainter(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(Uri.fromFile(File(data.image)))
                            .apply { crossfade(true) }
                            .build()
                    ),
                    contentDescription = "Expanded Image",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .height(350.dp) // 이미지 높이 설정
                        .background(Color.White)
                )

                Spacer(modifier = Modifier.height(16.dp))

                // 아이콘 섹션 (프로필, 좋아요, 저장, 위치)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center // 구문 수정
                ) {
                    // 사용자 프로필 및 ID
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        val thisUser = GlobalVariables.userList[data.owner]
                        Image(
                            painter = rememberAsyncImagePainter(
                                model = ImageRequest.Builder(LocalContext.current)
                                    .data(Uri.fromFile(File(thisUser.profile)))
                                    .apply { crossfade(true) }
                                    .build()
                            ),
                            contentDescription = "User Profile",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .border(1.dp, Color.White, CircleShape)
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = thisUser.username,
                            style = MaterialTheme.typography.bodySmall
                        )
                    }

                    Spacer(modifier = Modifier.width(20.dp)) // 아이콘 간격 조정

                    // 좋아요 아이콘
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        IconButton(
                            onClick = {
                                isRecommend = !isRecommend
                                if (isRecommend) {
                                    userdata.recommend.add(data.id)
                                    data.recommend += 1
                                    recommendCount += 1
                                } else {
                                    userdata.recommend.remove(data.id)
                                    data.recommend -= 1
                                    recommendCount -= 1
                                }
                            }
                        ) {
                            Icon(
                                imageVector = if (isRecommend) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                                contentDescription = "Recommend",
                                tint = if (isRecommend) Color.Red else Color.Gray,
                                modifier = Modifier.size(35.dp) // 크기 조정
                            )
                        }
                        Spacer(modifier = Modifier.height(0.dp)) // 좋아요 아이콘과 텍스트 간격 설정
                        Text(
                            text = "$recommendCount",
                            style = MaterialTheme.typography.bodySmall
                        )
                    }

                    Spacer(modifier = Modifier.width(20.dp)) // 아이콘 간격 조정

                    // 위치 아이콘
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            imageVector = Icons.Default.LocationOn,
                            contentDescription = "Location",
                            tint = Color.Black,
                            modifier = Modifier.size(28.dp) // 크기 조정
                        )
                        Spacer(modifier = Modifier.height(13.dp)) // 위치 아이콘과 텍스트 간격 설정
                        Text(
                            text = GlobalVariables.placeList[data.place].name, // 위치 정보 제거
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                    Spacer(modifier = Modifier.width(20.dp)) // 아이콘 간격 조정

                    // 저장 아이콘
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Image(
                            painter = painterResource(id = R.drawable.save_icon), // 업로드한 이미지 파일 참조
                            contentDescription = "Save Icon",
                            modifier = Modifier.size(25.dp) // 크기 조정
                        )
                        Spacer(modifier = Modifier.height(17.dp)) // 저장 아이콘과 텍스트 간격 설정
                        Text(
                            text = "저장",
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // 텍스트 내용 (후기란)
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFF90CAF9)) // 후기란 배경색 변경
                        .padding(16.dp) // 내용에 여백 추가
                ) {
                    Text(
                        text = data.text,
                        style = MaterialTheme.typography.bodyMedium,
                        textAlign = TextAlign.Center
                    )
                }
            }

            // 닫기 버튼
            IconButton(
                onClick = onClose,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .size(40.dp)
            ) {
                Icon(imageVector = Icons.Default.Close, contentDescription = "Close")
            }
        }
    }
}









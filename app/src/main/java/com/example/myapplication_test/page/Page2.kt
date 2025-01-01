package com.example.myapplication_test.page

import android.content.Context
import android.graphics.BitmapFactory
import android.media.ExifInterface
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
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
import com.example.myapplication_test.utils.pretendardFontFamily
import java.io.File


@Composable
fun ReviewGrid(context: Context) {
    var selectedLocation by remember { mutableStateOf<ReviewData?>(null) }
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
            // 스크롤 가능하도록 수정
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // 왼쪽 열
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    GlobalVariables.reviewList.filterIndexed { index, _ -> index % 2 == 0 }.forEach { location ->
                        var width by remember { mutableStateOf(0.0)}
                        val density = LocalDensity.current // 밀도 값 가져오기
                        val imageHeight = calculateImageHeight(location.image,
                            width = width)
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(16.dp))
                                .background(Color(0xFFE3F2FD))
                                .fillMaxWidth()
                                .height(imageHeight.dp)
                                .clickable { selectedLocation = location }
                                .onGloballyPositioned { coordinates ->
                                    width = (with(density) {coordinates.size.width.toDp()}).value.toDouble() // 현재 가로값 저장
                                }
                        ) {
                            Image(
                                painter = rememberAsyncImagePainter(
                                    model = Uri.fromFile(File(location.image))
                                ),
                                contentDescription = "Uploaded Image",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.fillMaxSize()
                            )
                        }
                    }
                }

                // 오른쪽 열
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    GlobalVariables.reviewList.filterIndexed { index, _ -> index % 2 != 0 }.forEach { location ->
                        var width by remember { mutableStateOf(0.0)}
                        val density = LocalDensity.current // 밀도 값 가져오기
                        val imageHeight = calculateImageHeight(location.image,
                            width = width)

                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(16.dp))
                                .background(Color(0xFFE3F2FD))
                                .fillMaxWidth()
                                .height(imageHeight.dp)
                                .clickable { selectedLocation = location }
                                .onGloballyPositioned { coordinates ->
                                    width = (with(density) {coordinates.size.width.toDp()}).value.toDouble() // 현재 가로값 저장
                                }
                        ) {
                            Image(
                                painter = rememberAsyncImagePainter(
                                    model = Uri.fromFile(File(location.image))
                                ),
                                contentDescription = "Uploaded Image",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.fillMaxSize()
                            )
                        }
                    }
                }
            }

            // 업로드 버튼
            TextButton(
                onClick = { writeReviewMode = true },
                shape = RoundedCornerShape(200.dp),
                colors = ButtonDefaults.textButtonColors(
                    containerColor = Color(0xFF57B1FF),
                    contentColor = Color.White
                ),
                modifier = Modifier
                    .size(100.dp)
                    .padding(16.dp)
                    .align(Alignment.BottomEnd)
            ) {
                Text("+", fontFamily = pretendardFontFamily,
                    fontWeight = FontWeight.Bold, color = Color.White, fontSize = 36.sp)
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

// 이미지 비율에 따라 동적으로 높이를 계산하는 함수
private fun calculateImageHeight(imagePath: String, width:Double): Int {
    val options = BitmapFactory.Options()
    options.inJustDecodeBounds = true
    BitmapFactory.decodeFile(imagePath, options)
    var imageWidth = options.outWidth
    var imageHeight = options.outHeight

    // EXIF 데이터 읽기
    val exif = ExifInterface(imagePath)
    val rotationDegrees = when (exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL)) {
        ExifInterface.ORIENTATION_ROTATE_90,
        ExifInterface.ORIENTATION_ROTATE_270 -> 90
        else -> 0
    }

    // 이미지가 회전된 경우 가로와 세로 크기 교환
    if (rotationDegrees == 90) {
        val temp = imageWidth
        imageWidth = imageHeight
        imageHeight = temp
    }

    // 가로 크기를 기준으로 비율에 따라 높이 계산
    val targetWidth = width // 기준 가로 크기 (dp)
    return (targetWidth * (imageHeight.toFloat() / imageWidth)).toInt()
}




@Composable
fun WriteReview(context: Context, onClose: () -> Unit, onUpload: (ReviewData) -> Unit) {
    val (imageUri, launcher) = getLocalImage()

    // 상태 저장
    var selectPlace by remember { mutableStateOf(0) }
    var reviewText by remember { mutableStateOf("") } // 후기 입력용 상태
    var satisfaction by remember { mutableStateOf(5) }
    var expanded by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFFFFFF)) // 전체 배경색
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
                    .clip(RoundedCornerShape(16.dp)) // 모서리 둥글게
                    .background(Color(0xFF57B1FF)) // 배경색
                    .clickable { launcher.launch("image/*") }
            ) {
                if (imageUri == null) {
                    Icon(
                        imageVector = Icons.Default.Add, // + 아이콘
                        contentDescription = "Upload Image",
                        modifier = Modifier
                            .align(Alignment.Center)
                            .size(60.dp),
                        tint = Color.White
                    )
                } else {
                    Image(
                        painter = rememberAsyncImagePainter(imageUri),
                        contentDescription = "Uploaded Image",
                        contentScale = ContentScale.Fit, // 원본 비율 유지
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(getImageAspectRatio(imageUri)) // 이미지 비율에 맞춤
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 장소 입력 섹션
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp)) // 모서리 둥글게
                    .background(Color(0xFF57B1FF))
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Place, // 장소 아이콘
                    contentDescription = "Location",
                    modifier = Modifier.size(24.dp),
                    tint = Color.White
                )
                Spacer(modifier = Modifier.width(8.dp))
                Button(onClick = { expanded = true }) {
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
                            text = { Text(text = item.name,
                                fontFamily = pretendardFontFamily,
                                fontWeight = FontWeight.Normal)
                            }
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
                Text("만족도", fontFamily = pretendardFontFamily,
                    fontWeight = FontWeight.SemiBold)
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
                    fontFamily = pretendardFontFamily,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.White
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 후기 작성 텍스트 섹션
            OutlinedTextField(
                value = reviewText, // 후기 입력 상태 사용
                onValueChange = { reviewText = it },
                placeholder = { Text("후기 작성해주세요",
                    fontFamily = pretendardFontFamily,
                    fontWeight = FontWeight.Normal) },
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
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF57B1FF))
            ) {
                Text("업로드", color = Color.White, fontFamily = pretendardFontFamily,
                    fontWeight = FontWeight.SemiBold)
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

// 이미지 비율을 계산하는 함수
@Composable
private fun getImageAspectRatio(imageUri: Uri): Float {
    val options = BitmapFactory.Options()
    options.inJustDecodeBounds = true
    val inputStream = LocalContext.current.contentResolver.openInputStream(imageUri)
    BitmapFactory.decodeStream(inputStream, null, options)
    inputStream?.close()
    return options.outWidth.toFloat() / options.outHeight.toFloat()
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
    var isSaved by remember { mutableStateOf(userdata.myPlaceList.contains(data.place)) }
    var recommendCount by remember { mutableIntStateOf(data.recommend) }

    Dialog(
        onDismissRequest = { onClose() }
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFFFFFFF)) // 전체 배경색
                .padding(16.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState()) // 스크롤 가능 추가
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
                    contentScale = ContentScale.Fit, // 원본 비율 유지하도록 수정
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(calculateImageAspectRatio(data.image)) // 이미지 비율 유지
                        .clip(RoundedCornerShape(16.dp))
                        .background(Color.White)
                )

                Spacer(modifier = Modifier.height(16.dp))

                // 아이콘 섹션 (프로필, 좋아요, 저장, 위치)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.Bottom,
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
                                .clickable { if(data.owner!=GlobalVariables.userID)showUser() }
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = thisUser.username,
                            fontFamily = pretendardFontFamily,
                            fontWeight = FontWeight.Normal
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
                            fontFamily = pretendardFontFamily,
                            fontWeight = FontWeight.Normal
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
                            fontFamily = pretendardFontFamily,
                            fontWeight = FontWeight.Normal
                        )
                    }
                    Spacer(modifier = Modifier.width(20.dp)) // 아이콘 간격 조정

                    // 저장 아이콘
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        IconButton(
                            onClick = {
                                isSaved = !isSaved
                                if (isSaved) {
                                    userdata.myPlaceList.add(data.place)
                                } else {
                                    userdata.myPlaceList.remove(data.place)
                                }
                            }
                        ) {
                            Icon(
                                painter = if (isSaved) painterResource(id = R.drawable.save_icon) else painterResource(id = R.drawable.unsave_icon),
                                contentDescription = "Recommend",
                                tint = Color.Black,
                                modifier = Modifier.size(25.dp) // 크기 조정
                            )
                        }
                        Spacer(modifier = Modifier.height(1.dp)) // 저장 아이콘과 텍스트 간격 설정
                        Text(
                            text = if (isSaved) "저장됨" else "저장",
                            fontFamily = pretendardFontFamily,
                            fontWeight = FontWeight.Normal
                        )
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // 텍스트 내용 (후기란)
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp) // 내용에 여백 추가
                ) {
                    Text(
                        text = data.text,
                        fontFamily = pretendardFontFamily,
                        fontWeight = FontWeight.Normal,
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

// 이미지 비율을 계산하는 함수
private fun calculateImageAspectRatio(imagePath: String): Float {
    val options = BitmapFactory.Options()
    options.inJustDecodeBounds = true
    BitmapFactory.decodeFile(imagePath, options)
    val imageWidth = options.outWidth
    val imageHeight = options.outHeight

    return imageWidth.toFloat() / imageHeight.toFloat()
}









package com.example.myapplication_test.page

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.example.myapplication_test.ReviewData
import com.example.myapplication_test.utils.decodeImageFromJsonString
import com.example.myapplication_test.utils.getLocalImage
import com.example.myapplication_test.utils.handleBitmapToBase64



// 사진 및 각자 객체
@Composable
fun ReviewGrid(context: Context, locationList: MutableList<ReviewData>) {
    var selectedLocation by remember { mutableStateOf<ReviewData?>(null) } // 선택된 이미지 상태
    var writeReviewMode by remember{ mutableStateOf(false) }
    var ImageReturnState by remember { mutableStateOf(false) }
    Box(modifier = Modifier.fillMaxSize()) {
        if(writeReviewMode){
            WriteReview(
                context,
                onClose = { writeReviewMode = false },
                onUpload={
                    ret->
                        ImageReturnState=true
                        locationList.add(ret)
                }
            )
            if(ImageReturnState){
                writeReviewMode=false
                ImageReturnState=false
            }
        }
        else if (selectedLocation == null) {
            // 기본 그리드
            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black),
                horizontalArrangement = Arrangement.spacedBy(1.dp),
                verticalArrangement = Arrangement.spacedBy(1.dp)
            ) {
                items(locationList) { location ->
                    ReviewItem(
                        data = location,
                        onItemClick = { selectedLocation = it } // 클릭 시 이미지 선택
                    )
                }
            }
            TextButton(
                onClick = {
                    writeReviewMode=true
                },
                shape = RoundedCornerShape(50.dp),
                colors = ButtonDefaults.textButtonColors(
                    containerColor = Color.Black, // 배경색 설정
                    contentColor = Color.White // 텍스트 색상
                ),
                modifier = Modifier
                    .width(90.dp)
                    .height(90.dp)
                    .padding(10.dp)
                    .align(Alignment.BottomEnd)
            ) {
                Text("글", color = Color.White, fontSize = 24.sp)
            }
        } else {
            // 확대된 이미지 뷰
            ExpandedReview(
                data = selectedLocation!!,
                onClose = { selectedLocation = null } // 닫기 버튼 클릭 시 상태 초기화
            )
        }
    }
}

@Composable
fun WriteReview(context: Context, onClose: () -> Unit, onUpload: (ReviewData) -> Unit) {
    val (imageUri, launcher) = getLocalImage()
    var retBase64 = "null"

    // 상태 저장
    var city by remember { mutableStateOf("") }
    var district by remember { mutableStateOf("") }
    var neighborhood by remember { mutableStateOf("") }
    var placeName by remember { mutableStateOf("") }
    var satisfaction by remember { mutableStateOf(5) }

    // 스크롤 상태
    val scrollState = rememberScrollState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {

        // 스크롤 가능한 Column
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState) // 스크롤 가능하게 설정
                .padding(top = 80.dp) // 닫기 버튼 공간 확보
        ) {
            // 이미지 버튼 및 이미지 출력
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f) // 정사각형
            ) {
                Button(
                    onClick = { launcher.launch("image/*") },
                    shape = RoundedCornerShape(0.dp), // 네모난 버튼
                    modifier = Modifier.fillMaxSize()
                ) {
                    Text("이미지 업로드")
                }
                imageUri?.let { uri ->
                    Image(
                        painter = rememberAsyncImagePainter(uri),
                        contentDescription = "Uploaded Image",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 입력 필드
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            ) {
                // 시
                TextField(
                    value = city,
                    onValueChange = { city = it },
                    label = { Text("시") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))

                // 구
                TextField(
                    value = district,
                    onValueChange = { district = it },
                    label = { Text("구") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))

                // 동
                TextField(
                    value = neighborhood,
                    onValueChange = { neighborhood = it },
                    label = { Text("동") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))

                // 장소 이름


                TextField(
                    value = placeName,
                    onValueChange = { placeName = it },
                    label = { Text("장소 이름") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(16.dp))

                // 만족도 슬라이더
                Text("만족도: $satisfaction", style = MaterialTheme.typography.bodyMedium)
                Slider(
                    value = satisfaction.toFloat(),
                    onValueChange = { satisfaction = it.toInt() },
                    valueRange = 1f..10f, // 1부터 10까지
                    steps = 9,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // JSON 추가 버튼
            Button(
                onClick = {
                    imageUri?.let { uri ->
                        val bitmapData = handleBitmapToBase64(context, uri)
                        if (bitmapData != "") {
                            retBase64 = bitmapData
                        }

                        onUpload(
                            ReviewData(
                            image=retBase64,
                            city = city,
                            district = district,
                            neighborhood = neighborhood,
                            name = placeName,
                            rating = satisfaction,
                            recommend = 0,
                            reviewId = 111
                        )
                        )
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text("JSON에 추가")
            }
        }
        // 닫기 버튼
        TextButton(
            onClick = onClose,
            shape = RoundedCornerShape(50.dp),
            colors = ButtonDefaults.textButtonColors(
                containerColor = Color.Black,
                contentColor = Color.White
            ),
            modifier = Modifier
                .width(60.dp)
                .height(60.dp)
                .padding(10.dp)
                .align(Alignment.TopStart)
        ) {
            Text("<", color = Color.White, fontSize = 24.sp)
        }
    }
}
@Composable
fun ReviewItem(data: ReviewData, onItemClick: (ReviewData) -> Unit) {
//    val imageResId = LocalContext.current.resources.getIdentifier(
//        data.image,
//        "drawable",
//        LocalContext.current.packageName
//    )
    Image(
        bitmap = decodeImageFromJsonString(data.image).asImageBitmap(),
        contentDescription = "Sample Image",
        contentScale = ContentScale.Crop,
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1f) // 정사각형
            .clickable { onItemClick(data) } // 클릭 이벤트 전달
    )
}


@Composable
fun ExpandedReview(data: ReviewData, onClose: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // 확대된 이미지
        Column (
            modifier = Modifier.fillMaxSize()
        ){
            Image(
                bitmap = decodeImageFromJsonString(data.image).asImageBitmap(),
                contentDescription = "Expanded Image",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f) // 정사각형
                    .padding(10.dp)
            )
            Text(text = "시(군): ${data.city}", style = MaterialTheme.typography.bodyMedium)
            Text(text = "구(면): ${data.district}", style = MaterialTheme.typography.bodyMedium)
            Text(text = "동(리): ${data.neighborhood}", style = MaterialTheme.typography.bodyMedium)
            Text(text = "별점: ${data.rating}", style = MaterialTheme.typography.bodyMedium)
            Text(text = "추천 수: ${data.recommend}", style = MaterialTheme.typography.bodyMedium)
        }
        // 닫기 버튼
        TextButton(
            onClick = onClose,
            shape = RoundedCornerShape(50.dp),
            colors = ButtonDefaults.textButtonColors(
                containerColor = Color.Black, // 배경색 설정
                contentColor = Color.White // 텍스트 색상
            ),
            modifier = Modifier
                .width(60.dp)
                .height(60.dp)
                .padding(10.dp)
                .align(Alignment.TopStart)
        ) {
            Text("<", color = Color.White, fontSize = 24.sp)
        }

    }
}

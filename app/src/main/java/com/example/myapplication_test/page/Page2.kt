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
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import com.example.myapplication_test.GlobalVariables
import com.example.myapplication_test.ReviewData
import com.example.myapplication_test.utils.decodeImageFromJsonString
import com.example.myapplication_test.utils.getLocalImage
import com.example.myapplication_test.utils.handleBitmapToBase64
import com.example.myapplication_test.utils.saveJson


// 사진 및 각자 객체
@Composable
fun ReviewGrid(context: Context) {
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
                    GlobalVariables.userList[GlobalVariables.userID].reviews.add(ret.id)
                    GlobalVariables.reviewList.add(ret)
                    saveJson(context,"review.json",GlobalVariables.reviewList)
                    saveJson(context,"users.json",GlobalVariables.userList)
                }
            )
            if(ImageReturnState){
                writeReviewMode=false
                ImageReturnState=false
            }
        }
        else if (selectedLocation == null) {
            // 기본 그리드
//            AndroidView(
//                factory = { inflater ->
//                    LayoutInflater.from(inflater).inflate(R.layout.recycler_view_layout, null) as RecyclerView
//                },
//                modifier = Modifier.fillMaxSize(),
//                update = { recyclerView ->
//                    recyclerView.layoutManager = GridLayoutManager(context, 3) // 3열 그리드
//                    recyclerView.adapter = ReviewAdapter(
//                        (0 until GlobalVariables.reviewList.size).toMutableList() ?: mutableListOf(),
//                        onItemClick = { selectedLocation = it }
//                    )
//                }
//            )
            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black),
                horizontalArrangement = Arrangement.spacedBy(1.dp),
                verticalArrangement = Arrangement.spacedBy(1.dp)
            ) {
                items(GlobalVariables.reviewList) { location ->
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
            selectedLocation?.let { location ->
                ExpandedReview(
                    data = location,
                    onClose = { selectedLocation = null }
                )
            }
        }
    }
    Text(text = "${GlobalVariables.reviewList.size}")
}

@Composable
fun WriteReview(context: Context, onClose: () -> Unit, onUpload: (ReviewData) -> Unit) {
    val (imageUri, launcher) = getLocalImage()
    var retUri = "null"

    // 상태 저장
    var selectPlace by remember { mutableStateOf(0) }
    var selectText by remember { mutableStateOf("") }
    var satisfaction by remember { mutableStateOf(5) }
    var expanded by remember { mutableStateOf(false) }

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
                Button(onClick = { expanded=true }) {
                    Text(text = GlobalVariables.placeList[selectPlace].name)
                }
                // 장소 이름
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
                Spacer(modifier = Modifier.height(16.dp))
                OutlinedTextField(
                    value = selectText,
                    onValueChange = { selectText = it },
                    label = { Text("후기글") },
                    placeholder = { Text("후기를 작성해주세요...") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp),
                    maxLines = 10,
                    singleLine = false // 다중 줄 입력 활성화
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
                                id= GlobalVariables.reviewList.size,
                                owner = GlobalVariables.userID,
                                image=retBase64,
                                rating = satisfaction,
                                recommend = 0,
                                place = selectPlace,
                                text = selectText
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
            Text(text = "${data.text}", style = MaterialTheme.typography.bodyMedium)
            Text(text = "별점: ${data.rating}", style = MaterialTheme.typography.bodyMedium)
            Text(text = "추천 수: ${data.recommend}", style = MaterialTheme.typography.bodyMedium)
            Text(text = "작성자: ${data.owner}", style = MaterialTheme.typography.bodyMedium)
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

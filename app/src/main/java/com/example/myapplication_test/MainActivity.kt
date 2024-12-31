package com.example.myapplication_test

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.example.myapplication_test.ui.theme.MyApplication_testTheme
import com.example.myapplication_test.utils.loadJson
import com.example.myapplication_test.utils.parseJson
import com.example.myapplication_test.utils.saveDrawableToInternalStorage
import com.example.myapplication_test.utils.saveJson
import kotlinx.serialization.Serializable

object GlobalVariables{
    var userID: Int = -1
    var userList: MutableList<UserData> = mutableListOf()
    var reviewList: MutableList<ReviewData> = mutableListOf()
    var placeList: List<PlaceData> = listOf()
    var contactList: List<ContactData> = listOf()
    var badgeList: List<BadgeData> = listOf()
    var testImg: String = ""
    var userSession by mutableStateOf(false)
}

@Serializable
data class UserData(
    val id: Int,
    var username: String,
    val userid: String,
    val password: String,
    var nationality: String,
    var profile: String, // uri
    val follower: MutableList<Int>, // Sorted
    val following: MutableList<Int>, // Sorted
    val recommend: MutableList<Int>, // Sorted
    val badgeCount: List<Int>, // Sorted
    val reviews: MutableList<Int>, // Unsorted
    val myPlaceList: MutableList<Int>, // Unsorted
)

@Serializable
data class ReviewData(
    val id: Int,
    val owner: Int,
    var recommend: Int,
    val rating: Int,
    val place: String,
    var image: String, // uri
    val text: String,
)

@Serializable
data class PlaceData(
    val id: Int,
    val name: String,
    val address: String,
    val lat: Float,
    val lon: Float,
    val imageRoot: String,
    val ratingAvg: Float,
    val badges: List<Int>,
)

@Serializable
data class ContactData(
    val id: Int,
    val name: String,
    val text: String,
    val imageRoot: String,
    val tel: String,
    val website: String,
)

@Serializable
data class BadgeData(
    val id: Int,
    val name: String,
    val text: String,
    val bronze: Int,
    val silver: Int,
    val gold: Int,
    val bronzeImageRoot: String,
    val silverImageRoot: String,
    val goldImageRoot: String,
)


class MainActivity : ComponentActivity() {
    private val handler = Handler(Looper.getMainLooper())
    private val runnable = object : Runnable {
        override fun run() {
            handler.postDelayed(this, 1000L)
            // 여기에 실행할 작업 작성
            saveJson(context = this@MainActivity,"users.json",GlobalVariables.userList)
            saveJson(context = this@MainActivity,"review.json",GlobalVariables.reviewList)

            // 5초 후 다시 실행
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // 리스트 데이터 다 불러오기
        GlobalVariables.reviewList = loadJson<ReviewData>(this,"review.json").toMutableList()
        GlobalVariables.userList= loadJson<UserData>(this,"users.json").toMutableList()
        GlobalVariables.contactList =  parseJson(this, "contact.json")
        GlobalVariables.placeList = parseJson(this, "place.json")
        GlobalVariables.badgeList = parseJson(this, "badge.json")
        GlobalVariables.testImg = saveDrawableToInternalStorage(this,R.drawable.test1_img,"testImg.jpg")

        handler.post(runnable)
        // 불러오기 종료
        setContent {
            MyApplication_testTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    if (GlobalVariables.userSession) {
                        // 로그인 성공 후 탭 화면 표시
                        TabLayout(context = this)
                    } else {
                        // 로그인 화면 표시
                        LoginScreen(
                            onLoginSuccess = {ret ->
                                GlobalVariables.userSession = true
                                GlobalVariables.userID = ret
                            },
                            onSignUp = { userid, password,nationality  ->
                                GlobalVariables.userList.add(
                                    UserData(
                                        GlobalVariables.userList.size,
                                        "사용자${GlobalVariables.userList.size}",
                                        userid,
                                        password,
                                        nationality,
                                        GlobalVariables.testImg,
                                        mutableListOf(),
                                        mutableListOf(),
                                        mutableListOf(),
                                        listOf(0,0,0),
                                        mutableListOf(),
                                        mutableListOf()
                                    )
                                )
                            }
                        )
                    }
                }
            }
        }
    }

    override fun onDestroy() {
        saveJson(context = this@MainActivity,"users.json",GlobalVariables.userList)
        saveJson(context = this@MainActivity,"review.json",GlobalVariables.reviewList)
        super.onDestroy()
        // Activity가 파괴되면 핸들러 중지
        handler.removeCallbacks(runnable)
    }
}
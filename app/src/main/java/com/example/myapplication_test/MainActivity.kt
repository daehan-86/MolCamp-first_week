package com.example.myapplication_test

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.example.myapplication_test.ui.theme.MyApplication_testTheme
import com.example.myapplication_test.utils.loadJson
import com.example.myapplication_test.utils.parseJson
import com.example.myapplication_test.utils.readJsonFile
import com.example.myapplication_test.utils.saveJson
import kotlinx.serialization.Serializable

object GlobalVariables{
    var userID: Int = -1
    var userList: MutableList<UserData> = mutableListOf()
    var reviewList: MutableList<ReviewData> = mutableListOf()
    var placeList: List<PlaceData> = listOf()
    var contactList: List<ContactData> = listOf()
    var testImg: String = ""
}

@Serializable
data class UserData(
    val id: Int,
    val username: String,
    val password: String,
    val nationality: String,
    var profile: String, // Base64
    val follower: List<Int>, // Sorted
    val following: List<Int>, // Sorted
    val recommend: List<Int>, // Sorted
    val reviews: List<Int>, // Unsorted
    val myPlaceList: List<Int>, // Unsorted
)

@Serializable
data class ReviewData(
    val id: Int,
    val owner: Int,
    val recommend: Int,
    val rating: Int,
    val place: Int,
    var image: String, // Base64
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
    val contacts: List<Int>,
)

@Serializable
data class ContactData(
    val id: Int,
    val name: String,
    val text: String,
    val imageRoot: String,
    val tel: String,
    val website: String
)


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // 리스트 데이터 다 불러오기
        GlobalVariables.reviewList = loadJson<ReviewData>(this,"review.json").toMutableList()
        GlobalVariables.userList= loadJson<UserData>(this,"users.json").toMutableList()
        GlobalVariables.contactList =  parseJson(this, "contact.json")
        GlobalVariables.placeList = parseJson(this, "place.json")
        GlobalVariables.testImg = this.readJsonFile("test1_img.txt")
        // 불러오기 종료
        setContent {
            MyApplication_testTheme {
                var isLoggedIn by remember { mutableStateOf(false) }
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    if (isLoggedIn) {
                        // 로그인 성공 후 탭 화면 표시
                        TabLayout(context = this)
                    } else {
                        // 로그인 화면 표시
                        LoginScreen(
                            onLoginSuccess = {ret ->
                                isLoggedIn = true
                                GlobalVariables.userID = ret
                            },
                            onSignUp = { username, password,nationality  ->
                                GlobalVariables.userList.add(UserData(GlobalVariables.userList.size,username, password, nationality, "null",
                                    listOf(), listOf(), listOf(), listOf(), listOf()))
                                saveJson(this, "users.json", GlobalVariables.userList)
                            }
                        )
                    }
                }
            }
        }
    }
}
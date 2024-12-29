package com.example.myapplication_test

import android.content.Context
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
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

object GlobalVariables{
    var userCount: Int = 0
    var reviewCount: Int = 0
    var userId: Int = -1
    var
}
@Serializable
data class UserData(
    val id: Int,
    val username: String,
    val password: String,
    val nationality: String,
    val profile: String, // Base64
    val follower: List<Int>, // Sorted
    val following: List<Int>, // Sorted
    val recommend: List<Int>, // Sorted
    val reviews: List<Int>, // Unsorted
    val placeList: List<Int>, // Unsorted
)

@Serializable
data class ReviewData(
    val id: Int,
    val owner: Int,
    val recommend: Int,
    val rating: Int,
    val place: Int,
    val Image: String, // Base64
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
)

@Serializable
data class ContactData(
    val title: String,
    val color: String,
    val dialogContent: String,
    val phoneNumber: String,
    val website: String
)


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val ReviewData = parseJson<ReviewData>(this,"review.json").toMutableList()
        val tempBitmap = this.readJsonFile("test1_img.txt")
        val defalultUserData:List<UserData> = parseJson(this, "users.json")
        val userData:List<UserData> = loadJson(this,"users.json")
        val users = (userData+defalultUserData).toMutableList()
        val imgData:List<ContactData> = parseJson(this, "contact.json")
        for(item in ReviewData){
            if(item.image=="null"){
                item.image = tempBitmap
            }
        }

        setContent {
            MyApplication_testTheme {
                var isLoggedIn by remember { mutableStateOf(false) }
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    if (isLoggedIn) {
                        // 로그인 성공 후 탭 화면 표시
                        TabLayout(
                            context = this,
                            imgData,
                            ReviewData
                        )
                    } else {
                        // 로그인 화면 표시
                        LoginScreen(
                            data = users,
                            onLoginSuccess = { isLoggedIn = true },
                            onSignUp = { username, password ->
                                users.add(UserData(username, password))
                                saveJson(this, "users.json", users)
                            }
                        )
                    }
                }
            }
        }
    }
    private fun saveJson(context: Context, fileName: String, data: List<UserData>) {
        val jsonString = Json.encodeToString(data)
        context.openFileOutput(fileName, Context.MODE_PRIVATE).use {
            it.write(jsonString.toByteArray())
        }
    }
}
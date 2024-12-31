package com.example.myapplication_test.ui

import android.content.Context
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.myapplication_test.GlobalVariables

@Composable
fun TabSection(context: Context, showID: Int) {
    var selectedTab by remember { mutableStateOf(0) }
    val tabs = listOf("내 후기", "여행 갈곳")
    if(showID==GlobalVariables.userID){
        Column(modifier = Modifier.fillMaxSize()) {
            // Tab Buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                tabs.forEachIndexed { index, title ->
                    Text(
                        text = title,
                        modifier = Modifier
                            .clickable { selectedTab = index }
                            .padding(8.dp),
                        style = if (selectedTab == index) {
                            MaterialTheme.typography.bodyLarge
                        } else {
                            MaterialTheme.typography.bodyMedium
                        }
                    )
                }
            }

            // Tab Contents
            when (selectedTab) {
                0 -> RecyclerViewScreen(context, showID)
                1 -> Text("여행 갈곳", modifier = Modifier.fillMaxSize())
            }
        }
    } else{
        RecyclerViewScreen(context, showID)
    }
}
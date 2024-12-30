package com.example.myapplication_test.ui

import android.content.Context
import android.view.LayoutInflater
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication_test.GlobalVariables
import com.example.myapplication_test.R
import com.example.myapplication_test.ReviewAdapter
import com.example.myapplication_test.ReviewData
import com.example.myapplication_test.page.ExpandedReview

@Composable
fun RecyclerViewScreen(context: Context) {
    var selectedLocation by remember { mutableStateOf<ReviewData?>(null) }
    if(selectedLocation==null){
        AndroidView(
            factory = { inflater ->
                LayoutInflater.from(inflater).inflate(R.layout.recycler_view_layout, null) as RecyclerView
            },
            modifier = Modifier.fillMaxSize(),
            update = { recyclerView ->
                recyclerView.layoutManager = GridLayoutManager(context, 3) // 3열 그리드
                recyclerView.adapter = ReviewAdapter(
                    GlobalVariables.userList[GlobalVariables.userID]?.reviews ?: mutableListOf(),
                    onItemClick = { selectedLocation = it }
                )
            }
        )
    } else{
        selectedLocation?.let { location ->
            ExpandedReview(
                data = location,
                onClose = { selectedLocation = null }
            )
        }
    }
}
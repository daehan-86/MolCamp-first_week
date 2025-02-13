package com.example.myapplication_test.ui

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.recyclerview.widget.RecyclerView
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.example.myapplication_test.GlobalVariables
import com.example.myapplication_test.R
import com.example.myapplication_test.ReviewData
import java.io.File


class ReviewAdapter(
    private val reviews: MutableList<Int>,
    private val onItemClick: (ReviewData) -> Unit, // 클릭 이벤트 전달
    private val isProfile: Boolean = true
) : RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder>() {

    class ReviewViewHolder(itemView: View, private val isProfile: Boolean) : RecyclerView.ViewHolder(itemView) {
        private val composeView: ComposeView = itemView.findViewById(R.id.compose_view)

        fun bind(data: ReviewData, onItemClick: (ReviewData) -> Unit) {
            // ComposeView를 통해 Compose 컴포저블 렌더링
            composeView.setContent {
                Image(
                    painter = rememberAsyncImagePainter(
                        ImageRequest.Builder(LocalContext.current)
                            .data(Uri.fromFile(File(data.image)))
                            .crossfade(true)
                            .build()
                    ),
                    contentDescription = null,
                    contentScale = if (isProfile) ContentScale.Crop else ContentScale.Fit,
                    modifier = if (isProfile) {
                        Modifier
                            .fillMaxWidth() // 그리드 셀 폭 = match_parent
                            .aspectRatio(1f)
                            .padding(4.dp) // 이미지 간격 추가
                            .clip(RoundedCornerShape(16.dp)) // Corner radius 적용
                            .clickable { onItemClick(data) }
                    } else {
                        Modifier
                            .fillMaxSize()
                            .padding(4.dp) // 이미지 간격 추가
                            .clip(RoundedCornerShape(16.dp)) // Corner radius 적용
                            .clickable { onItemClick(data) }
                    }
                )
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReviewViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_review, parent, false)
        return ReviewViewHolder(view, isProfile)
    }

    override fun onBindViewHolder(holder: ReviewViewHolder, position: Int) {
        val reviewData = GlobalVariables.reviewList[reviews[position]]
        holder.bind(reviewData, onItemClick)
    }

    override fun getItemCount(): Int = reviews.size
}


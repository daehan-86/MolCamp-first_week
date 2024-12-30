package com.example.myapplication_test

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.ComposeView
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication_test.utils.decodeImageFromJsonString


class ReviewAdapter(
    private val reviews: MutableList<Int>,
    private val onItemClick: (ReviewData) -> Unit // 클릭 이벤트 전달
) : RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder>() {

    class ReviewViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val composeView: ComposeView = itemView.findViewById(R.id.compose_view)

        fun bind(data: ReviewData, onItemClick: (ReviewData) -> Unit) {
            // ComposeView를 통해 Compose 컴포저블 렌더링
            composeView.setContent {
                Image(
                    bitmap = decodeImageFromJsonString(data.image).asImageBitmap(),
                    contentDescription = "Sample Image",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(1f) // 정사각형 비율 유지
                        .clickable { onItemClick(data) } // 클릭 이벤트 전달
                )
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReviewViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_review, parent, false)
        return ReviewViewHolder(view)
    }

    override fun onBindViewHolder(holder: ReviewViewHolder, position: Int) {
        val reviewData = GlobalVariables.reviewList[reviews[position]]
        holder.bind(reviewData, onItemClick)
    }

    override fun getItemCount(): Int = reviews.size
}

//package com.example.myapplication_test.ui
//
//class StaggeredImageAdapter<ImageItem>(
//    private val items: List<ImageItem>,
//    private val onItemClick: ((ImageItem) -> Unit)? = null
//) : RecyclerView.Adapter<StaggeredImageAdapter<Any?>.StaggeredViewHolder>() {
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StaggeredViewHolder {
//        val inflater = LayoutInflater.from(parent.context)
//        // item_staggered_image.xml inflate
//        val view = inflater.inflate(R.layout.item_staggered_image, parent, false)
//        return StaggeredViewHolder(view)
//    }
//
//    override fun onBindViewHolder(holder: StaggeredViewHolder, position: Int) {
//        holder.bind(items[position], onItemClick)
//    }
//
//    override fun getItemCount(): Int = items.size
//
//    class StaggeredViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
//        private val itemImageView = itemView.findViewById<ImageView>(R.id.itemImageView)
//
//        fun bind(item: ImageItem, onItemClick: ((ImageItem) -> Unit)?) {
//            // Glide (또는 Coil)로 이미지 로드
//            Glide.with(itemImageView.context)
//                .load(item.imagePath) // 로컬 경로나 URL 가능
//                .into(itemImageView)
//
//            // 클릭 리스너
//            itemImageView.setOnClickListener {
//                onItemClick?.invoke(item)
//            }
//        }
//    }
//}
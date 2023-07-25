package com.jju.rainbow_magazine

import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class RecyclerView_news_Adapter(
    private val context: Context,
    private val newsList: MutableList<RecyclerView_news_item>
) : RecyclerView.Adapter<RecyclerView_news_Adapter.NewsViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.activity_news, parent, false)
        return NewsViewHolder(view)
    }


    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        val news = newsList[position]
        holder.titleTextView.text = news.title
        holder.contentTextView.text = news.content
        holder.dateTextView.text = news.date

        val bitmap = BitmapFactory.decodeByteArray(news.image, 0, news.image.size)
        holder.imageView.setImageBitmap(bitmap)

        holder.itemView.setOnClickListener {
            val intent = Intent(context, Activity_News_detail::class.java)
            intent.putExtra("news_id", news.id)
            context.startActivity(intent)
        }

        val username = SharedPreferencesUtil.getUsername(context)
        val dbHelper = DBHelper(context)
        if (username != null && dbHelper.checkAdmin(username)) {
            holder.deleteImageView.visibility = View.VISIBLE // 관리자일 경우 보이게 설정
        } else {
            holder.deleteImageView.visibility = View.GONE // 관리자가 아닐 경우 숨김
        }

        // 삭제 아이콘에 클릭 리스너 설정
        holder.deleteImageView.setOnClickListener {
            DBHelper(context).deleteNews(news.id)
            newsList.removeAt(position)
            notifyItemRemoved(position)
            notifyItemRangeChanged(position, newsList.size)
        }
    }

    override fun getItemCount() = newsList.size

    class NewsViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val titleTextView: TextView = view.findViewById(R.id.tv_rv_news_title)
        val contentTextView: TextView = view.findViewById(R.id.tv_rv_news_subtitle)
        val dateTextView: TextView = view.findViewById(R.id.tv_rv_news_date)
        val imageView: ImageView = view.findViewById(R.id.img_rv_news_photo)
        val deleteImageView: ImageView = view.findViewById(R.id.img_rv_news_delete) // 삭제 아이콘 뷰
    }
}

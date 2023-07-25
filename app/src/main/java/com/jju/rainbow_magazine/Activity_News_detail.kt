package com.jju.rainbow_magazine

import android.graphics.BitmapFactory
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class Activity_News_detail : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_news_detail)

        val backButton: ImageView = findViewById(R.id.backButton)
        backButton.setOnClickListener {
            finish()
        }

        val newsId = intent.getIntExtra("news_id", -1) // Get news ID from intent
        val dbHelper = DBHelper(this)
        val news = dbHelper.getNews(newsId)

        if (news != null) {
            if (news != null) {
                val newsImageView: ImageView = findViewById(R.id.img_rv_news_detail_photo)
                newsImageView.setImageBitmap(BitmapFactory.decodeByteArray(news.image ?: byteArrayOf(), 0, news.image?.size ?: 0))

                val news_titleTextView: TextView = findViewById(R.id.img_rv_news_detail_content)
                news_titleTextView.text = news.title

                val news_dateTextView: TextView = findViewById(R.id.img_rv_news_detail_date)
                news_dateTextView.text = news.date

                // Subcontent TextView 추가
                val news_subcontentTextView: TextView = findViewById(R.id.img_rv_news_detail_subcontent)
                news_subcontentTextView.text = news.content
            } else {
                // Handle error: no news found with the given ID
            }
        }
    }
}

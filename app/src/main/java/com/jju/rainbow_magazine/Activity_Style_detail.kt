package com.jju.rainbow_magazine

import android.graphics.BitmapFactory
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class Activity_Style_detail : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_style_detail)

        val backButton: ImageView = findViewById(R.id.backButton)
        backButton.setOnClickListener {
            finish()
        }

        val styleId = intent.getIntExtra("style_id", -1)
        val dbHelper = DBHelper(this)
        val style = dbHelper.getStyle(styleId)

        if (style != null) {
            if (style != null) {
                val styleImageView: ImageView = findViewById(R.id.img_rv_style_detail_photo)
                styleImageView.setImageBitmap(BitmapFactory.decodeByteArray(style.style_image ?: byteArrayOf(), 0, style.style_image?.size ?: 0))

                val style_titleTextView: TextView = findViewById(R.id.img_rv_style_detail_content)
                style_titleTextView.text = style.style_name

                val style_dateTextView: TextView = findViewById(R.id.img_rv_style_detail_age)
                style_dateTextView.text = style.style_age

                // Subcontent TextView 추가
                val style_subcontentTextView: TextView = findViewById(R.id.img_rv_style_detail_item)
                style_subcontentTextView.text = style.style_content
            } else {
                // Handle error: no news found with the given ID
            }
        }
    }
}


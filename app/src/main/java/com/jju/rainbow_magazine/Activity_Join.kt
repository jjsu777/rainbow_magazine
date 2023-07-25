package com.jju.rainbow_magazine;

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView

class Activity_Join : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_join)


        val backButton: ImageView = findViewById(R.id.backButton)
        backButton.setOnClickListener {
            finish()
        }
    }
}
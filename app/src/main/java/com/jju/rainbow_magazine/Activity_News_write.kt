package com.jju.rainbow_magazine

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.DatePicker
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.io.ByteArrayOutputStream

class Activity_News_write : AppCompatActivity() {
    private lateinit var news_content: EditText
    private lateinit var news_write_complete: Button
    private lateinit var image_upload: ImageView
    private lateinit var image_button: Button
    private lateinit var news_title: EditText
    private lateinit var news_date: DatePicker
    private var selectedImageUri: Uri? = null
    private val REQUEST_GALLERY = 1001

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_news_write)
        val backButton: ImageView = findViewById(R.id.backButton)
        backButton.setOnClickListener {
            finish()
        }
        news_title = findViewById(R.id.news_title)
        news_content = findViewById(R.id.news_content)
        news_date = findViewById(R.id.news_datePicker)
        news_write_complete = findViewById(R.id.news_write_complete)
        image_upload = findViewById(R.id.news_image_upload)
        image_button = findViewById(R.id.news_image_button)

        image_button.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, REQUEST_GALLERY)
        }

        news_write_complete.setOnClickListener {
            val title = news_title.text.toString()
            val content = news_content.text.toString()
            val image: ByteArray? = selectedImageUri?.let { uri -> getBytes(uri) }
            val year = news_date.year
            val month = news_date.month + 1
            val day = news_date.dayOfMonth
            val date = "$year/$month/$day"
            val dbHelper = DBHelper(this)

            val result = if (image != null) {
                dbHelper.insertNews(null, title, content, image, date)
            } else {
                false
            }

            if (result) {
                Toast.makeText(this, "뉴스 작성 완료", Toast.LENGTH_SHORT).show()
                // Set the result to indicate success
                setResult(Activity.RESULT_OK)
                // Finish this Activity and go back to the Fragment_News
                finish()
            } else {
                Toast.makeText(this, "뉴스 작성 실패", Toast.LENGTH_SHORT).show()
            }
        }
    }




        override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && requestCode == REQUEST_GALLERY) {
            selectedImageUri = data?.data
            image_upload.setImageURI(selectedImageUri)
        }
    }

    private fun getBytes(uri: Uri): ByteArray {
        val inputStream = contentResolver.openInputStream(uri)
        val bitmap = BitmapFactory.decodeStream(inputStream)
        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
        return stream.toByteArray()
    }
}

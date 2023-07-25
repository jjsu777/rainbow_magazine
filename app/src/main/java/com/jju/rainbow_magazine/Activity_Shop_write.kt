package com.jju.rainbow_magazine

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.io.ByteArrayOutputStream

class Activity_Shop_write : AppCompatActivity() {
    private lateinit var shop_product_content: EditText
    private lateinit var shop_write_complete: Button
    private lateinit var shop_image_upload: ImageView
    private lateinit var shop_image_button: Button
    private lateinit var shop_product_title: EditText
    private lateinit var shop_product_price: EditText
    private var selectedImageUri: Uri? = null
    private val REQUEST_GALLERY = 1001

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shop_write)
        val backButton: ImageView = findViewById(R.id.backButton)
        backButton.setOnClickListener {
            finish()
        }
        shop_product_title = findViewById(R.id.shop_product_title)
        shop_product_content = findViewById(R.id.shop_product_content)
        shop_product_price = findViewById(R.id.shop_product_price)
        shop_write_complete = findViewById(R.id.shop_write_complete)
        shop_image_upload = findViewById(R.id.shop_image_upload)
        shop_image_button = findViewById(R.id.shop_image_button)

        shop_image_button.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, REQUEST_GALLERY)
        }

        shop_write_complete.setOnClickListener {
            val title = shop_product_title.text.toString()
            val content = shop_product_content.text.toString()
            val image: ByteArray? = selectedImageUri?.let { uri -> getBytes(uri) }
            val price = shop_product_price.text.toString()
            val dbHelper = DBHelper(this)

            val result = if (image != null) {
                dbHelper.insertShop(null, title, content, image, price)
            } else {
                false
            }

            if (result) {
                Toast.makeText(this, "상품 작성 완료", Toast.LENGTH_SHORT).show()
                // Set the result to indicate success
                setResult(Activity.RESULT_OK)
                // Finish this Activity and go back to the Fragment_News
                finish()
            } else {
                Toast.makeText(this, "상품 작성 실패", Toast.LENGTH_SHORT).show()
            }
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && requestCode == REQUEST_GALLERY) {
            selectedImageUri = data?.data
            shop_image_upload.setImageURI(selectedImageUri)
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
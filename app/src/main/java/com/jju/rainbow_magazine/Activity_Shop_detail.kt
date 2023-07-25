package com.jju.rainbow_magazine

import android.graphics.BitmapFactory
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.jju.rainbow_magazine.databinding.ActivityShopDetailBinding

class Activity_Shop_detail : AppCompatActivity() {
    private val binding by lazy {
        ActivityShopDetailBinding.inflate(layoutInflater)
    }

    private lateinit var shopPaymentProcessor: ShopPaymentProcessor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val backButton: ImageView = findViewById(R.id.backButton)
        backButton.setOnClickListener {
            finish()
        }

        val shopId = intent.getIntExtra("shop_id", -1)
        val dbHelper = DBHelper(this)
        val shop = dbHelper.getShop(shopId)

        if (shop != null) {
            val shopImageView: ImageView = findViewById(R.id.img_rv_shop_detail_photo)
            shopImageView.setImageBitmap(BitmapFactory.decodeByteArray(shop.shop_image ?: byteArrayOf(), 0, shop.shop_image?.size ?: 0))

            val shop_titleTextView: TextView = findViewById(R.id.img_rv_shop_detail_title)
            shop_titleTextView.text = shop.shop_title

            val shop_dateTextView: TextView = findViewById(R.id.img_rv_shop_detail_price)
            shop_dateTextView.text = shop.shop_price +"원"

            // Subcontent TextView 추가
            val shop_subcontentTextView: TextView = findViewById(R.id.img_rv_shop_detail_content)
            shop_subcontentTextView.text = shop.shop_content

            shopPaymentProcessor = ShopPaymentProcessor(supportFragmentManager, this)

            binding.buybutton.setOnClickListener {
                shopPaymentProcessor.goRequest(shop.shop_title, shop.shop_price)
            }
        } else {
            // Handle error: no shop found with the given ID
        }
    }
}

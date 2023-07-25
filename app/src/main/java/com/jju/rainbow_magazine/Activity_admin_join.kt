package com.jju.rainbow_magazine;

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import com.kakao.sdk.common.util.Utility

class Activity_admin_join : AppCompatActivity() {

    private lateinit var dbHelper: DBHelper

   //관리자 계정 생성
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        dbHelper = DBHelper(this)

        val isInserted = dbHelper.insertUser("admin", "admin@example.com", "admin", true)

        if (isInserted) {
            Log.d("LoginActivity", "관리자 계정 생성 성공.")
        } else {
            Log.d("LoginActivity", "관리자 계정 생성 실패.")
        }
    }
}
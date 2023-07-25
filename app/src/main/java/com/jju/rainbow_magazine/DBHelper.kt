package com.jju.rainbow_magazine

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DBHelper(context: Context?):
    SQLiteOpenHelper(context, "Login.db", null, 40) {
    override fun onCreate(MyDB: SQLiteDatabase) {
        MyDB.execSQL("create Table users(username TEXT primary key, email TEXT, password TEXT, isAdmin BOOLEAN DEFAULT 0)")  // isAdmin 추가
        MyDB.execSQL("create Table news(newswrite_id INTEGER PRIMARY KEY AUTOINCREMENT, title TEXT , content TEXT, image BLOB,  date TEXT)")
        MyDB.execSQL("create Table style(stylewrite_id INTEGER PRIMARY KEY AUTOINCREMENT, style_title TEXT , style_content TEXT, style_image BLOB,  style_date TEXT)")
        MyDB.execSQL("create Table shop(shopwrite_id INTEGER PRIMARY KEY AUTOINCREMENT, shop_title TEXT , shop_content TEXT, shop_image BLOB,  shop_price TEXT)")

    }

    override fun onUpgrade(MyDB: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        MyDB.execSQL("drop Table if exists users")
        MyDB.execSQL("drop Table if exists news")
        MyDB.execSQL("drop Table if exists style")
        MyDB.execSQL("drop Table if exists shop")
        onCreate(MyDB)
    }

    fun changePassword(username: String, currentPassword: String, newPassword: String): Boolean {
        val MyDB = this.writableDatabase
        val cursor = MyDB.rawQuery(
            "Select * from users where username = ? and password =?",
            arrayOf(username, currentPassword)
        )

        return if (cursor.count > 0) {
            cursor.close()

            val contentValues = ContentValues().apply {
                put("password", newPassword)
            }

            val numRowsUpdated = MyDB.update("users", contentValues, "username=?", arrayOf(username))
            numRowsUpdated > 0
        } else {
            false
        }
    }

    fun insertShop(shopwrite_id: String?, shop_title: String?, shop_content: String?, shop_image: ByteArray?, shop_price: String): Boolean { // 뉴스 데이터 삽입 메서드
        val MyDB = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put("shopwrite_id", shopwrite_id)
        contentValues.put("shop_title", shop_title)
        contentValues.put("shop_content", shop_content)
        contentValues.put("shop_image", shop_image)
        contentValues.put("shop_price", shop_price)
        val result = MyDB.insert("shop", null, contentValues)
        return result != -1L
    }

    fun deleteShop(shopwrite_id: Int): Boolean {
        val db = this.writableDatabase
        val result = db.delete("shop", "shopwrite_id=?", arrayOf(shopwrite_id.toString()))
        return result != 0
    }

    fun getAllShop(): Cursor {
        val db = this.readableDatabase
        return db.rawQuery("SELECT * FROM shop", null)
    }

//스타일 디비 시작
    fun insertStyle(stylewrite_id: String?, style_title: String?, style_content: String?, style_image: ByteArray?, style_date: String): Boolean { // 뉴스 데이터 삽입 메서드
        val MyDB = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put("stylewrite_id", stylewrite_id)
        contentValues.put("style_title", style_title)
        contentValues.put("style_content", style_content)
        contentValues.put("style_image", style_image)
        contentValues.put("style_date", style_date)
        val result = MyDB.insert("style", null, contentValues)
        return result != -1L
    }

    fun deleteStyle(stylewrite_id: Int): Boolean {
        val db = this.writableDatabase
        val result = db.delete("style", "stylewrite_id=?", arrayOf(stylewrite_id.toString()))
        return result != 0
    }

    fun getAllStyle(): Cursor {
        val db = this.readableDatabase
        return db.rawQuery("SELECT * FROM style", null)
    }

    fun insertNews(newswrite_id: String?, title: String?, content: String?, image: ByteArray?, date: String): Boolean { // 뉴스 데이터 삽입 메서드
        val MyDB = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put("newswrite_id", newswrite_id)
        contentValues.put("title", title)
        contentValues.put("content", content)
        contentValues.put("image", image)
        contentValues.put("date", date)
        val result = MyDB.insert("news", null, contentValues)
        return result != -1L
    }

    fun deleteNews(newswrite_id: Int): Boolean {
        val db = this.writableDatabase
        val result = db.delete("news", "newswrite_id=?", arrayOf(newswrite_id.toString()))
        return result != 0
    }

    fun getAllNews(): Cursor {
        val db = this.readableDatabase
        return db.rawQuery("SELECT * FROM news", null)
    }

    @SuppressLint("Range")
    fun getShop(id: Int): RecyclerView_shop_item? {
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM shop WHERE shopwrite_id = ?", arrayOf(id.toString()))

        return if (cursor.moveToFirst()) {
            // Create a News object from the data in the cursor
            val shop = RecyclerView_shop_item(
                cursor.getInt(cursor.getColumnIndex("shopwrite_id")),
                cursor.getString(cursor.getColumnIndex("shop_title")),
                cursor.getString(cursor.getColumnIndex("shop_content")),
                cursor.getString(cursor.getColumnIndex("shop_price")),
                cursor.getBlob(cursor.getColumnIndex("shop_image")) // Assuming the image is stored as a blob
            )
            cursor.close()
            shop
        } else {
            cursor.close()
            null
        }
    }

    @SuppressLint("Range")
    fun getStyle(id: Int): RecyclerView_style_item? {
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM style WHERE stylewrite_id = ?", arrayOf(id.toString()))

        return if (cursor.moveToFirst()) {
            // Create a News object from the data in the cursor
            val style = RecyclerView_style_item(
                cursor.getInt(cursor.getColumnIndex("stylewrite_id")),
                cursor.getString(cursor.getColumnIndex("style_title")),
                cursor.getString(cursor.getColumnIndex("style_content")),
                cursor.getString(cursor.getColumnIndex("style_date")),
                cursor.getBlob(cursor.getColumnIndex("style_image"))
            )
            cursor.close()
            style
        } else {
            cursor.close()
            null
        }
    }

    @SuppressLint("Range")
    fun getNews(id: Int): RecyclerView_news_item? {
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM news WHERE newswrite_id = ?", arrayOf(id.toString()))

        return if (cursor.moveToFirst()) {
            // Create a News object from the data in the cursor
            val news = RecyclerView_news_item(
                cursor.getInt(cursor.getColumnIndex("newswrite_id")),
                cursor.getString(cursor.getColumnIndex("title")),
                cursor.getString(cursor.getColumnIndex("content")),
                cursor.getString(cursor.getColumnIndex("date")),
                cursor.getBlob(cursor.getColumnIndex("image")) // Assuming the image is stored as a blob
            )
            cursor.close()
            news
        } else {
            cursor.close()
            null
        }
    }

    fun insertUser(username: String?, email: String?, password: String?, isAdmin: Boolean): Boolean {
        val MyDB = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put("username", username)
        contentValues.put("email", email)
        contentValues.put("password", password)
        contentValues.put("isAdmin", if (isAdmin) 1 else 0)  // isAdmin 추가
        val result = MyDB.insert("users", null, contentValues)
        return if (result == -1L) false else true
    }

    fun checkAdmin(username: String): Boolean {
        val MyDB = this.readableDatabase
        val cursor = MyDB.rawQuery(
            "Select * from users where username = ? and isAdmin = 1",
            arrayOf(username)
        )
        val isAdmin = cursor.count > 0
        cursor.close()
        return isAdmin
    }
    fun checkUsername(username: String, email: String, password: String): Boolean {
        val MyDB = this.writableDatabase
        var res = true
        val cursor = MyDB.rawQuery(
            "Select * from users where username =? and email = ? and password = ?",
            arrayOf(username, email, password)
        )
        if (cursor.count <= 0) res = false
        return res
    }


    fun checkUserpass(username: String, password: String): Boolean {
        val MyDB = this.writableDatabase
        var res = true
        var cursor = MyDB.rawQuery(
            "Select * from users where username = ? and password =?",
            arrayOf(username, password)
        )
        if (cursor.count <= 0) res = false
        return res
    }

    companion object {
        const val DBNAME = "Login.db"

    }
}
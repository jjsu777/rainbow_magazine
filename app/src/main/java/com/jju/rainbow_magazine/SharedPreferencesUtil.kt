package com.jju.rainbow_magazine

import android.content.Context

object SharedPreferencesUtil {
    private const val USER_PREFERENCES = "USER_PREFERENCES"
    private const val LOGGED_IN = "LOGGED_IN"
    private const val USERNAME = "USERNAME"

    fun isLoggedIn(context: Context): Boolean {
        val sharedPref = context.getSharedPreferences(USER_PREFERENCES, Context.MODE_PRIVATE)
        return sharedPref.getBoolean(LOGGED_IN, false)
    }

    fun setLoggedIn(context: Context, isLoggedIn: Boolean, username: String) {
        val sharedPref = context.getSharedPreferences(USER_PREFERENCES, Context.MODE_PRIVATE)
        with(sharedPref.edit()) {
            putBoolean(LOGGED_IN, isLoggedIn)
            putString(USERNAME, username)
            apply()
        }
    }

    fun setLoggedInKakao(context: Context, isLoggedIn: Boolean) {
        val sharedPref = context.getSharedPreferences(USER_PREFERENCES, Context.MODE_PRIVATE)
        with(sharedPref.edit()) {
            putBoolean(LOGGED_IN, isLoggedIn)
            apply()
        }
    }


    fun getUsername(context: Context): String? {
        val sharedPreferences = context.getSharedPreferences(USER_PREFERENCES, Context.MODE_PRIVATE)
        return sharedPreferences.getString(USERNAME, null)
    }

    fun logOut(context: Context) {
        val sharedPref = context.getSharedPreferences(USER_PREFERENCES, Context.MODE_PRIVATE)
        with(sharedPref.edit()) {
            putBoolean(LOGGED_IN, false)
            remove(USERNAME)
            apply()
        }
    }
}

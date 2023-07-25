package com.jju.rainbow_magazine

import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class Activity_Home : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        val backButton: ImageView = findViewById(R.id.backButton)
        backButton.setOnClickListener {
            onBackPressed()
        }
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, Fragment_Shop())
                .commit()
        }


        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)

        bottomNavigationView.setOnNavigationItemSelectedListener { menuItem ->
            var selectedFragment: Fragment = Fragment_Shop()
            when (menuItem.itemId) {
                R.id.home -> selectedFragment = Fragment_Shop()
                R.id.news -> {
                    selectedFragment = if (SharedPreferencesUtil.isLoggedIn(this)) {
                        Fragment_News()
                    } else {
                        Fragment_News_notlogin()
                    }
                }

                R.id.style -> {
                selectedFragment = if (SharedPreferencesUtil.isLoggedIn(this)) {
                    Fragment_Style()
                } else {
                    Fragment_Style_notlogin()
                }
            }

                R.id.my -> {
                    selectedFragment = if (SharedPreferencesUtil.isLoggedIn(this)) {
                        Fragment_My()
                    } else {
                        Fragment_Login()
                    }
                }
            }

            val transaction = supportFragmentManager.beginTransaction()
            transaction.replace(R.id.fragment_container, selectedFragment)
            transaction.commit()
            true
        }
        bottomNavigationView.selectedItemId = R.id.home
    }
    override fun onBackPressed() {
        if (supportFragmentManager.backStackEntryCount > 0) {
            supportFragmentManager.popBackStack()
        } else {
            super.onBackPressed()
        }
    }
}


package com.example.weatherapplication.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.weatherapplication.R
import com.example.weatherapplication.fragment.WeatherMainFragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, WeatherMainFragment.newInstance())
                .commitNow()
        }
    }
}
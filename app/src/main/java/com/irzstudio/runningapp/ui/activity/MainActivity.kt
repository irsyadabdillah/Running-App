package com.irzstudio.runningapp.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.irzstudio.runningapp.R
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        actionBar()
    }

    private fun actionBar(){
        setSupportActionBar(menu_toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(false)
        supportActionBar?.title = "Run"
    }
}
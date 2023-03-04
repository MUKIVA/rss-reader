package com.mukiva.rssreader

import androidx.appcompat.app.AppCompatActivity

open class ReturnedActivity : AppCompatActivity() {

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}
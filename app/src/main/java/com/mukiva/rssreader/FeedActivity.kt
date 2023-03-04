package com.mukiva.rssreader

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.mukiva.rssreader.databinding.ActivityFeedBinding

class FeedActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFeedBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFeedBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.addRssButton.setOnClickListener {
            goAddRss()
        }

        binding.openItemButton.setOnClickListener {
            goItem()
        }
    }

    private fun goAddRss() {
        val intent = Intent(this, AddRssActivity::class.java)
        startActivity(intent)
    }

    private fun goItem() {
        val intent = Intent(this, FeedItemActivity::class.java)
        startActivity(intent)
    }
}
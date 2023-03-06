package com.mukiva.rssreader

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.mukiva.rssreader.databinding.ActivityFeedMainBinding

class FeedActivityMain : AppCompatActivity() {

    private lateinit var binding: ActivityFeedMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFeedMainBinding.inflate(layoutInflater)
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
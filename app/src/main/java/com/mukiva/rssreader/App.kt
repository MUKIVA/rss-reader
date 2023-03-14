package com.mukiva.rssreader

import android.app.Application
import com.mukiva.rssreader.watchfeeds.data.FeedItemsService

class App : Application() {

    val feedItemsService = FeedItemsService()

}
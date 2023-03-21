package com.mukiva.rssreader

import android.app.Application
import com.mukiva.rssreader.addrss.data.MockSearchRssService
import com.mukiva.rssreader.watchfeeds.data.MockFeedService

class App : Application() {
    val feedsService = MockFeedService()
    val SearchService = MockSearchRssService()
}
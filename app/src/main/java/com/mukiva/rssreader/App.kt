package com.mukiva.rssreader

import android.app.Application
import com.mukiva.rssreader.addrss.data.MockSearchRssService
import com.mukiva.rssreader.watchfeeds.data.MockFeedService
import timber.log.Timber

class App : Application() {
    val feedsService = MockFeedService()
    val searchService = MockSearchRssService()

    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }
}
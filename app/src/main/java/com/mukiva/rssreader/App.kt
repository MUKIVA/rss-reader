package com.mukiva.rssreader

import android.app.Application
import com.mukiva.rssreader.addrss.data.HttpSearchRssService
import com.mukiva.rssreader.addrss.data.SearchRssService
import com.mukiva.rssreader.addrss.parsing.entity.MyObjectBox
import com.mukiva.rssreader.watchfeeds.data.ORMRssService
import com.mukiva.rssreader.watchfeeds.data.RssService
import io.objectbox.BoxStore
import timber.log.Timber

class App : Application() {
    lateinit var feedsService: RssService
    lateinit var searchService: SearchRssService

    lateinit var store: BoxStore
        private set

    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }

        store = MyObjectBox.builder()
            .androidContext(this)
            .build()
        feedsService = ORMRssService(store)
        searchService = HttpSearchRssService()
    }
}

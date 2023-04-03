package com.mukiva.rssreader

import android.app.Application
import com.mukiva.rssreader.addrss.data.HttpSearchRssGateway
import com.mukiva.rssreader.addrss.data.SearchRssGateway
import com.mukiva.rssreader.addrss.data.parsing.entity.MyObjectBox
import com.mukiva.rssreader.watchfeeds.data.ORMRssStorage
import com.mukiva.rssreader.watchfeeds.data.RssStorage
import io.objectbox.BoxStore
import timber.log.Timber

class App : Application() {
    lateinit var feedsService: RssStorage
    lateinit var searchService: SearchRssGateway

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
        feedsService = ORMRssStorage(store)
        searchService = HttpSearchRssGateway()
    }
}

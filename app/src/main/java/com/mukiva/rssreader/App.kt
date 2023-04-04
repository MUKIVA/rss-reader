package com.mukiva.rssreader

import android.app.Application
import com.mukiva.rssreader.addrss.data.HttpRssSearchGateway
import com.mukiva.rssreader.addrss.data.RssSearchGateway
import com.mukiva.rssreader.addrss.data.parsing.entity.MyObjectBox
import com.mukiva.rssreader.watchfeeds.data.ORMRssStorage
import com.mukiva.rssreader.watchfeeds.data.RssStorage
import io.objectbox.BoxStore
import timber.log.Timber

class App : Application() {
    lateinit var rssStorage: RssStorage
    lateinit var searchGateway: RssSearchGateway

    private lateinit var store: BoxStore

    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }

        store = MyObjectBox.builder()
            .androidContext(this)
            .build()
        rssStorage = ORMRssStorage(store)
        searchGateway = HttpRssSearchGateway()
    }
}

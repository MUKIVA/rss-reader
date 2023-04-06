package com.mukiva.rssreader

import android.app.Application
import com.mukiva.rssreader.addrss.data.HttpRssSearchGateway
import com.mukiva.rssreader.addrss.data.RssSearchGateway
import com.mukiva.rssreader.addrss.data.parsing.entity.MyObjectBox
import com.mukiva.rssreader.watchfeeds.data.ORMRssStorage
import com.mukiva.rssreader.watchfeeds.domain.RssStorage
import io.objectbox.BoxStore
import okhttp3.OkHttpClient
import timber.log.Timber

class App : Application() {
    lateinit var rssStorage: RssStorage
    lateinit var searchGateway: RssSearchGateway

    private lateinit var _store: BoxStore

    private val _client: OkHttpClient = OkHttpClient.Builder()
        .cache(null)
        .build()

    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }

        _store = MyObjectBox.builder()
            .androidContext(this)
            .build()
        rssStorage = ORMRssStorage(_store)
        searchGateway = HttpRssSearchGateway(_client)
    }
}

package com.mukiva.rssreader

import android.app.Application
import com.mukiva.rssreader.addrss.data.HttpRssSearchGateway
import com.mukiva.rssreader.addrss.data.RssSearchGateway
import com.mukiva.rssreader.watchfeeds.data.ORMRssStorage
import com.mukiva.rssreader.watchfeeds.domain.RssStorage
import io.objectbox.BoxStore
import okhttp3.OkHttpClient

class App : Application() {
    lateinit var rssStorage: RssStorage
    lateinit var searchGateway: RssSearchGateway

    private lateinit var _store: BoxStore

    private val _client: OkHttpClient = OkHttpClient.Builder()
        .cache(null)
        .build()

    override fun onCreate() {
        super.onCreate()

//        _store = MyObjectBox.builder()
//            .androidContext(this)
//            .build()

        rssStorage = ORMRssStorage(_store)
        searchGateway = HttpRssSearchGateway(_client)
    }
}

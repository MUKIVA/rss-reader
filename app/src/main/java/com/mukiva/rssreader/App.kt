package com.mukiva.rssreader

import android.app.Application
import androidx.work.Configuration
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.mukiva.data.services.DataWorkFactory
import com.mukiva.data.services.UpdateAllRssWorker
import dagger.hilt.android.HiltAndroidApp
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltAndroidApp
class App : Application() {

    @Inject
    lateinit var workerFactory: DataWorkFactory

    private val mUpdateRssWorkRequest =
        PeriodicWorkRequestBuilder<UpdateAllRssWorker>(
            repeatInterval = UPDATE_RSS_PERIOD,
            repeatIntervalTimeUnit = TimeUnit.MILLISECONDS
        ).build()

    override fun onCreate() {
        super.onCreate()
        WorkManager.initialize(
            applicationContext,
            Configuration.Builder()
                .setExecutor(Executors.newFixedThreadPool(THREAD_POOL))
                .setWorkerFactory(workerFactory)
                .build()
        )

        WorkManager
            .getInstance(applicationContext)
            .enqueueUniquePeriodicWork(
                UpdateAllRssWorker.TAG,
                ExistingPeriodicWorkPolicy.KEEP,
                mUpdateRssWorkRequest
            )
    }

    companion object {
        private const val UPDATE_RSS_PERIOD = 30L * 60L * 1000L
        private const val THREAD_POOL = 8
    }
}


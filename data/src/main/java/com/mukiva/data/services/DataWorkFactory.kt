package com.mukiva.data.services

import android.content.Context
import androidx.work.ListenableWorker
import androidx.work.WorkerFactory
import androidx.work.WorkerParameters
import com.mukiva.data.repository.IRssMetaRepository
import com.mukiva.data.repository.IRssRepository
import javax.inject.Inject

class DataWorkFactory @Inject constructor(
    private val metaRepo: IRssMetaRepository,
    private val rssRepo: IRssRepository
) : WorkerFactory() {

    override fun createWorker(
        appContext: Context,
        workerClassName: String,
        workerParameters: WorkerParameters
    ): ListenableWorker? {
        return when(workerClassName) {
            UpdateAllRssWorker::class.java.name ->
                UpdateAllRssWorker(
                    appContext = appContext,
                    params = workerParameters,
                    metaRepo = metaRepo,
                    repo = rssRepo
                )
            else -> null
        }
    }

}
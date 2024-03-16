package com.mukiva.data.services

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.mukiva.data.repository.IRssMetaRepository
import com.mukiva.data.repository.IRssRepository

class UpdateAllRssWorker(
    appContext: Context,
    params: WorkerParameters,
    private val metaRepo: IRssMetaRepository,
    private val repo: IRssRepository
) : CoroutineWorker(appContext, params) {
    override suspend fun doWork(): Result {
        return updateAllSavedRss()
    }

    private suspend fun updateAllSavedRss(): Result {
        return try {
            val meta = metaRepo.getAllRssMeta()
            meta.forEach {
                repo.updateItems(it.originalUrl)
            }
            Log.d("UpdateAllRssWorker", "SUCCESS")
            Result.success()
        } catch (e: Exception) {
            Log.d("UpdateAllRssWorker", "${e.message}")
            Result.failure()
        }
    }

    companion object {
        const val TAG = "RequestAllRssWorker"
    }
}
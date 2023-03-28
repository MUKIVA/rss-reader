
package com.mukiva.rssreader.okhttp

import kotlinx.coroutines.suspendCancellableCoroutine
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Response
import okio.IOException

open class BaseOkHttpSource(
    private val _callbacks: AsyncCallCallbacks
) {

    companion object {
        @JvmStatic
        protected val client: OkHttpClient = OkHttpClient()
    }

    suspend fun Call.suspendEnqueue(): Response {
        return suspendCancellableCoroutine { continuation ->

            continuation.invokeOnCancellation {
                _callbacks.onCancel()
            }

            enqueue(object: Callback {

                override fun onFailure(call: Call, e: IOException) {
                    _callbacks.onFail(call, e, continuation)
                }

                override fun onResponse(call: Call, response: Response) {
                    _callbacks.onSuccess(call, response, continuation)
                }

            })
        }
    }
}
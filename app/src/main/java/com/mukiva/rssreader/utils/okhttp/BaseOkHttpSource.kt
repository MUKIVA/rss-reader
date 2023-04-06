
package com.mukiva.rssreader.utils.okhttp

import kotlinx.coroutines.suspendCancellableCoroutine
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Response
import okio.IOException

open class BaseOkHttpSource {

    suspend fun Call.suspendEnqueue(callbacks: AsyncCallCallbacks): Response {
        return suspendCancellableCoroutine { continuation ->

            continuation.invokeOnCancellation {
                callbacks.onCancel(continuation)
            }

            enqueue(object: Callback {

                override fun onFailure(call: Call, e: IOException) {
                    callbacks.onFail(call, e, continuation)
                }

                override fun onResponse(call: Call, response: Response) {
                    callbacks.onSuccess(call, response, continuation)
                }

            })

        }
    }
}
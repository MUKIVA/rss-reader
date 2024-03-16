
package com.mukiva.data.okhttp

import kotlinx.coroutines.CancellableContinuation
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okio.IOException
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

abstract class BaseOkHttpSource {

    private suspend fun Call.suspendEnqueue(callbacks: AsyncCallCallbacks): Response {
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

    suspend fun OkHttpClient.makeCall(request: Request): Response {
        return withContext(Dispatchers.IO) {
            val job = async {
                this@makeCall.newCall(request).suspendEnqueue(object : AsyncCallCallbacks {
                    override fun onCancel(continuation: CancellableContinuation<Response>) {
                        continuation.cancel()
                    }

                    override fun onFail(
                        call: Call,
                        e: IOException,
                        continuation: CancellableContinuation<Response>
                    ) {
                        continuation.resumeWithException(e)
                    }

                    override fun onSuccess(
                        call: Call,
                        response: Response,
                        continuation: CancellableContinuation<Response>
                    ) {
                        continuation.resume(response)
                    }

                })
            }
            job.await()
        }
    }
}
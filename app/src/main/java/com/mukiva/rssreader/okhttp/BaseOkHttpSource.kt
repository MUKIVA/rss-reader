
package com.mukiva.rssreader.okhttp

import com.mukiva.rssreader.addrss.domain.SearchException
import kotlinx.coroutines.suspendCancellableCoroutine
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Response
import okio.IOException
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

open class BaseOkHttpSource {

    companion object {
        @JvmStatic
        protected val client: OkHttpClient = OkHttpClient()
    }

    suspend fun Call.suspendEnqueue(): Response {
        return suspendCancellableCoroutine { continuation ->

            continuation.invokeOnCancellation {
                cancel()
                throw SearchException.TimeOutException()
            }

            enqueue(object: Callback {

                override fun onFailure(call: Call, e: IOException) {
                    continuation.resumeWithException(SearchException.ConnectionException(e))
                }

                override fun onResponse(call: Call, response: Response) {
//                    response.use {
                        if (response.isSuccessful) {
                            continuation.resume(response)
                        } else {
                            continuation.resumeWithException(SearchException.BackendException(response.code))
                        }
//                    }
                }

//                override fun onFailure(call: Call, e: IOException) {
//                    e.printStackTrace()
//                }
//
//                override fun onResponse(call: Call, response: Response) {
//                    response.use {
//                        if (!response.isSuccessful) throw IOException("Unexpected code $response")
//
//                        for ((name, value) in response.headers) {
//                            println("$name: $value")
//                        }
//
//                        println(response.body!!.string())
//                    }
//                }

            })
        }
    }
}
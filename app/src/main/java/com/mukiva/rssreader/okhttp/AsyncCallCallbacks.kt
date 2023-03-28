package com.mukiva.rssreader.okhttp

import kotlinx.coroutines.CancellableContinuation
import okhttp3.Call
import okhttp3.Response
import okio.IOException

interface AsyncCallCallbacks {
    fun onCancel()
    fun onFail(call: Call, e: IOException, continuation: CancellableContinuation<Response>)
    fun onSuccess(call: Call, response: Response, continuation: CancellableContinuation<Response>)
}
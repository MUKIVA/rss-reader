package com.mukiva.data.okhttp

import kotlinx.coroutines.CancellableContinuation
import okhttp3.Call
import okhttp3.Response
import okio.IOException

interface AsyncCallCallbacks {
    fun onCancel(continuation: CancellableContinuation<Response>)
    fun onFail(call: Call, e: IOException, continuation: CancellableContinuation<Response>)
    fun onSuccess(call: Call, response: Response, continuation: CancellableContinuation<Response>)
}
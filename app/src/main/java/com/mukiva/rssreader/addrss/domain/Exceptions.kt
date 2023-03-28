package com.mukiva.rssreader.addrss.domain

sealed class SearchException : RuntimeException {

    constructor() : super()
    constructor(cause: Throwable) : super(cause)

    class InvalidUrlException : SearchException()
    class TimeOutException : SearchException()
    data class ConnectionException(override val cause: Throwable) : SearchException(cause)
    data class BackendException(val code: Int) : SearchException()
}


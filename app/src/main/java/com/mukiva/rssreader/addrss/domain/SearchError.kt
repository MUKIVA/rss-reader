package com.mukiva.rssreader.addrss.domain

sealed interface SearchError

object InvalidUrlError : SearchError
object ConnectionError : SearchError
object TimeoutError : SearchError
object UnknownError : SearchError
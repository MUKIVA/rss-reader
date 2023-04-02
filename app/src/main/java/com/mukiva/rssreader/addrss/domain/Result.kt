package com.mukiva.rssreader.addrss.domain

sealed interface Result<out T>
class Success<out T>(val data: T) :
    Result<T>
class Error(val error: SearchError) :
    Result<Nothing>
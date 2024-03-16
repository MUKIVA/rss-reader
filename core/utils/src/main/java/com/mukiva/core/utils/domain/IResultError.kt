package com.mukiva.core.utils.domain

sealed interface IResultError {
    data object InvalidUrlError : IResultError
    data object UrlAlreadyExists : IResultError
    data object ConnectionError : IResultError
    data object TimeoutError : IResultError
    data object UnknownError : IResultError

}


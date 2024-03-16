package com.mukiva.core.utils.domain

sealed interface IResult<out T> {
    class Success<out T>(val data: T) : IResult<T>
    class Error(val resultError: IResultError) : IResult<Nothing>

}

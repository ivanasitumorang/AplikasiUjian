package com.azuka.aplikasiujian.base


/**
 * Created by ivanaazuka on 8/15/21.
 * Android Engineer
 */

sealed class Result<out R> {
    data class Success<out T>(val data: T) : Result<T>()
    data class Error(val exception: Exception) : Result<Nothing>()
}

suspend fun <T, S> Result<T>.next(nextFunc: suspend (data: T) -> Result<S>): Result<S> =
    when (this) {
        is Result.Success -> nextFunc(this.data)
        is Result.Error -> this
    }

suspend fun <T> Result<T>.onSuccess(codeBlock: suspend (data: T) -> Unit): Result<T> {
    if (this is Result.Success) codeBlock(data)

    return this
}

suspend fun <T> Result<T>.onError(codeBlock: suspend (exception: Exception) -> Unit): Result<T> {
    if (this is Result.Error) codeBlock(exception)

    return this
}
package com.azuka.aplikasiujian.base


/**
 * Created by ivanaazuka on 8/15/21.
 * Android Engineer
 */

sealed class Result<out R> {
    data class Success<out T>(val data: T) : Result<T>()
    data class Error(val exception: Exception) : Result<Nothing>()
}
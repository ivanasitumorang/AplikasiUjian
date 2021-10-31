package com.azuka.aplikasiujian.base


/**
 * Created by ivanaazuka on 31/10/21.
 * Android Engineer
 */

suspend fun <T> suspendTryCatch(
    codeBlock: suspend () -> Result<T>
): Result<T> = try {
    codeBlock.invoke()
} catch (e: Exception) {
    Result.Error(e)
}
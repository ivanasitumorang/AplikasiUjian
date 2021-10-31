package com.azuka.aplikasiujian.repository

import com.azuka.aplikasiujian.base.Result


/**
 * Created by ivanaazuka on 31/10/21.
 * Android Engineer
 */

interface AuthenticationRepository {
    suspend fun isUserLoggedIn(): Result<String>
    suspend fun login(loginReq: LoginReq): Result<String>
    suspend fun register(registerReq: RegisterReq): Result<Unit>
    suspend fun getUserActive(userId: String): Result<User>
}
package com.azuka.aplikasiujian.repository

import com.azuka.aplikasiujian.base.Result
import com.azuka.aplikasiujian.base.next
import com.azuka.aplikasiujian.base.suspendTryCatch
import com.azuka.aplikasiujian.data.authentication.AuthenticationDataSource
import com.azuka.aplikasiujian.data.firestore.FirestoreDataSource

class AuthenticationRepositoryImpl constructor(
    private val authData: AuthenticationDataSource,
    private val firestoreData: FirestoreDataSource
) : AuthenticationRepository {
    override suspend fun isUserLoggedIn(): Result<String> = authData.isUserLoggedIn()

    override suspend fun login(loginReq: LoginReq): Result<String> =
        authData.login(loginReq.email, loginReq.password)

    override suspend fun register(registerReq: RegisterReq): Result<Unit> = suspendTryCatch {
        authData.register(registerReq.email, registerReq.password).next {
            when (val result = firestoreData.saveUser(registerReq)) {
                is Result.Success -> result

                is Result.Error -> {
                    authData.deleteCurrentUser()
                }
            }
        }
    }

    override suspend fun getUserActive(userId: String): Result<User> =
        firestoreData.getUserActive(userId)
}
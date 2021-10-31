package com.azuka.aplikasiujian.data.authentication

import com.azuka.aplikasiujian.base.Result
import com.azuka.aplikasiujian.base.suspendTryCatch
import com.google.firebase.auth.FirebaseAuth
import javax.inject.Inject


/**
 * Created by ivanaazuka on 31/10/21.
 * Android Engineer
 */

class AuthenticationDataSource @Inject constructor(
    private val auth: FirebaseAuth
) {
    fun isUserLoggedIn(): Result<String> {
        val user = auth.currentUser
        return if (user == null) Result.Success("")
        else Result.Success(user.uid)
    }

    suspend fun deleteCurrentUser(): Result<Unit> = suspendTryCatch {
        val currentUser = auth.currentUser
        val isDeleted = currentUser?.delete()?.result != null
        if (isDeleted) Result.Success(Unit)
        else Result.Error(Exception("Gagal menghapus user"))
    }

    suspend fun login(email: String, password: String): Result<String> = suspendTryCatch {
        val userId = auth.signInWithEmailAndPassword(email, password).result.user?.uid
        if (userId == null) Result.Error(Exception("User gagal masuk"))
        else Result.Success(userId)
    }

    suspend fun register(email: String, password: String): Result<String> = suspendTryCatch {
        val userId = auth.createUserWithEmailAndPassword(email, password).result.user?.uid
        if (userId == null) Result.Error(Exception("User gagal daftar"))
        else Result.Success(userId)
    }
}
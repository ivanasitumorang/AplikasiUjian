package com.azuka.aplikasiujian.data.firestore

import com.azuka.aplikasiujian.base.Result
import com.azuka.aplikasiujian.base.suspendTryCatch
import com.azuka.aplikasiujian.data.Constants
import com.azuka.aplikasiujian.repository.RegisterReq
import com.azuka.aplikasiujian.repository.User
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.toObject
import javax.inject.Inject

class FirestoreDataSource @Inject constructor(
    private val database: FirebaseFirestore
) {
    suspend fun getUserActive(userId: String): Result<User> = suspendTryCatch {
        val userCollectionRef = database.collection(Constants.Collection.USERS)
        val user = userCollectionRef.document(userId).get().result.toObject<User>()
        if (user == null) {
            Result.Error(Exception("Gagal memuat data user dari database"))
        } else {
            Result.Success(user)
        }
//            .addOnCompleteListener { task ->
//                if (task.isSuccessful) {
//                    if (task.result.exists()) {
//                        val user = User
//                        Result.Success()
//                    } else {
//                        setupUserRole(account)
//                    }
//                } else {
//                    setupUserRole(account)
//                }
//            }

//        database.collection(Constants.Collection.USERS).document(userId)
//            .get().addOnSuccessListener { docSnapshot ->
//                val user = docSnapshot.toObject<User>()!!
//                if (user != null) {
//                    Result.Success(user)
//                } else {
//                    Result.Error(Exception("User tidak ada dalam database"))
//                }
//            }.addOnFailureListener { e ->
//                Result.Error(Exception("Gagal memuat data user dari database"))
//            }
    }

    suspend fun saveUser(registerReq: RegisterReq): Result<Unit> = suspendTryCatch {
        val userCollectionRef = database.collection(Constants.Collection.USERS)
        val isSaveSuccess = userCollectionRef.document(registerReq.id)
            .set(registerReq, SetOptions.merge())
            .isSuccessful
        if (isSaveSuccess) Result.Success(Unit)
        else Result.Error(Exception("Gagal menyimpan user"))
    }
}
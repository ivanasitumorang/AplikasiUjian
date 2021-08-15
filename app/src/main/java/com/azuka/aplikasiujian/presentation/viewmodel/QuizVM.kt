package com.azuka.aplikasiujian.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.azuka.aplikasiujian.base.Result
import com.azuka.aplikasiujian.data.Constants.Collection
import com.azuka.aplikasiujian.data.Question
import com.azuka.aplikasiujian.data.User
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject


/**
 * Created by ivanaazuka on 8/15/21.
 * Android Engineer
 */

@HiltViewModel
class QuizVM @Inject constructor(
    private val database: FirebaseFirestore
) : ViewModel() {


    fun createQuestion(quizName: String, question: Question) {
        viewModelScope.launch(Dispatchers.IO) {
//            data
        }
    }

    private val _createUserStatus = MutableLiveData<Result<Boolean>>()
    val createUserStatus: LiveData<Result<Boolean>> get() = _createUserStatus
    fun createUser(user: User) {
        viewModelScope.launch(Dispatchers.IO) {
            database.collection(Collection.USERS).document(user.id)
                .set(user, SetOptions.merge())
                .addOnSuccessListener {
                    _createUserStatus.postValue(Result.Success(true))
                }.addOnFailureListener { e ->
                    _createUserStatus.postValue(Result.Error(e))
                }
        }
    }
}
package com.azuka.aplikasiujian.feature.splashscreen.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.azuka.aplikasiujian.base.onError
import com.azuka.aplikasiujian.base.onSuccess
import com.azuka.aplikasiujian.repository.AuthenticationRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject


/**
 * Created by ivanaazuka on 31/10/21.
 * Android Engineer
 */

@HiltViewModel
class SplashscreenVM @Inject constructor(
    private val authRepository: AuthenticationRepository
) : ViewModel() {

    init {
        isUserAlreadyLoggedIn()
    }

    private val _userLog = MutableLiveData<UserLog>()
    val userLog: LiveData<UserLog> get() = _userLog
    fun isUserAlreadyLoggedIn() {
        viewModelScope.launch(Dispatchers.IO) {
            authRepository.isUserLoggedIn()
                .onSuccess { userId ->
                    if (userId.isNotEmpty()) {
                        authRepository.getUserActive(userId)
                            .onSuccess {
                                _userLog.postValue(
                                    UserLog(isLoggedIn = true, role = it.role)
                                )
                            }.onError {
                                _userLog.postValue(
                                    UserLog(isLoggedIn = false, role = -1)
                                )
                            }
                    } else {
                        _userLog.postValue(
                            UserLog(isLoggedIn = false, role = -1)
                        )
                    }
                }.onError {
                    _userLog.postValue(
                        UserLog(isLoggedIn = false, role = -1)
                    )
                }
        }
    }

}
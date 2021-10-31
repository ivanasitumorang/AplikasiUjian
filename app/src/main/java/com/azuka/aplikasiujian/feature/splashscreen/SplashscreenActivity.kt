package com.azuka.aplikasiujian.feature.splashscreen

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.azuka.aplikasiujian.R
import com.azuka.aplikasiujian.feature.authentication.AuthenticationActivity
import com.azuka.aplikasiujian.feature.splashscreen.viewmodel.SplashscreenVM
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SplashscreenActivity : AppCompatActivity() {

    private val viewModel by viewModels<SplashscreenVM>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splashscreen)

        viewModel.userLog.observe(this, { userLog ->
            if (userLog.isLoggedIn && userLog.role != -1) {
                Log.i("Hasil", "user logged in : ${userLog.role}")
            } else {
                Log.i("Hasil", "user belum logged in")
                startActivity(Intent(this, AuthenticationActivity::class.java))
                finish()
            }
        })
    }

}
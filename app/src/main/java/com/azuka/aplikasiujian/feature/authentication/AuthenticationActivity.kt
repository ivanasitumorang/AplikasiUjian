package com.azuka.aplikasiujian.feature.authentication

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import com.azuka.aplikasiujian.R

class AuthenticationActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_authentication)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_auth) as? NavHostFragment
        val navController = navHostFragment?.navController
    }
}
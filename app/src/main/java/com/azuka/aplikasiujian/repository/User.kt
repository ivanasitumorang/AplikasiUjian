package com.azuka.aplikasiujian.repository


/**
 * Created by ivanaazuka on 31/10/21.
 * Android Engineer
 */

data class User(
    val id: String,
    val name: String,
    val email: String,
    val phone: String,
    val photoUrl: String? = null,
    val role: Int
)

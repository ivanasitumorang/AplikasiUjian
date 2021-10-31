package com.azuka.aplikasiujian.repository


/**
 * Created by ivanaazuka on 31/10/21.
 * Android Engineer
 */

data class RegisterReq(
    val name: String,
    val id: String,
    val email: String,
    val phone: String,
    val password: String,
    val role: Int
)

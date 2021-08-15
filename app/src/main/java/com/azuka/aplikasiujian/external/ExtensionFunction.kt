package com.azuka.aplikasiujian.external


/**
 * Created by ivanaazuka on 8/15/21.
 * Android Engineer
 */
 
fun String.removeAllSpaces(): String {
    return this.replace("\\s".toRegex(), "")
}
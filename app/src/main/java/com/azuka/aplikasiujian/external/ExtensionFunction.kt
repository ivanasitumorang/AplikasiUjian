package com.azuka.aplikasiujian.external

import android.view.View
import org.joda.time.DateTime
import java.util.*


/**
 * Created by ivanaazuka on 8/15/21.
 * Android Engineer
 */
 
fun String.removeAllSpaces(): String {
    return this.replace("\\s".toRegex(), "")
}

fun String.toDateTime(): DateTime {
    return DateTime(this)
}

fun DateTime.formatToDay(): String {
//    val day = dayOfWeek().getAsText(Locale("in", "ID"))
//    val dateFormatter = DateTimeFormat.forPattern("EEEE, dd MMM yyyy")
    return toString("EEEE, dd MMM yyyy", Locale("in", "ID"))
}

fun DateTime.formatToTime(): String {
    return toString("HH.mm", Locale("in", "ID"))
}

fun View.show() {
    visibility = View.VISIBLE
}

fun View.hide() {
    visibility = View.GONE
}

fun View.invisible() {
    visibility = View.INVISIBLE
}
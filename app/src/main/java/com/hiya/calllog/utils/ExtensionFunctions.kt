package com.hiya.calllog.utils

import android.provider.CallLog
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*

fun String.getReadableDate(): String? {
    try {
        val date = Date(this.toLong())
        val format = SimpleDateFormat("HH:mm a MM/dd/yyyy")
        return format.format(date)
    } catch (e: Exception) {
        return null
    }
}

fun Long.getReadableDate(): String? {
    try {
        val date = Date(this)
        val format = SimpleDateFormat("HH:mm a MM/dd/yyyy")
        return format.format(date)
    } catch (e: Exception) {
        return null
    }
}

fun Int.getCallType(): String {
    var callType = ""
    when (this) {
        CallLog.Calls.OUTGOING_TYPE -> callType = "Outgoing"
        CallLog.Calls.INCOMING_TYPE -> callType = "Incoming"
    }
    return callType
}
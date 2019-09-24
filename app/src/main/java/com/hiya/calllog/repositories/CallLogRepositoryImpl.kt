package com.hiya.calllog.repositories

import android.content.Context
import android.support.v4.content.CursorLoader
import com.hiya.calllog.models.CallLog
import com.hiya.calllog.utils.getCallType
import com.hiya.calllog.utils.getReadableDate
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.launch
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class CallLogRepositoryImpl @Inject constructor(private val context: Context) : CallLogRepository {
    override fun getCallLogs(callback: (MutableMap<String, List<CallLog>>) -> Unit) {
        val callLogs = mutableMapOf<String, List<CallLog>>()

        launch (UI) {
            val cursorLoader = CursorLoader(context, android.provider.CallLog.Calls.CONTENT_URI,
                null,
                android.provider.CallLog.Calls.TYPE + " =? OR " + android.provider.CallLog.Calls.TYPE + " =? ",
                arrayOf(android.provider.CallLog.Calls.OUTGOING_TYPE.toString(), android.provider.CallLog.Calls.INCOMING_TYPE.toString()),
                android.provider.CallLog.Calls.DATE + " DESC")
            val cursor = async (CommonPool) {
                cursorLoader.loadInBackground()
            }

            cursor.await()?.let {
                val numberIndex = it.getColumnIndex(android.provider.CallLog.Calls.NUMBER)
                val dateIndex = it.getColumnIndex(android.provider.CallLog.Calls.DATE)
                val typeIndex = it.getColumnIndex(android.provider.CallLog.Calls.TYPE)

                while (it.moveToNext() && callLogs.size < 50) {
                    val date = Date(it.getString(dateIndex).toLong())
                    val format = SimpleDateFormat("dd")
                    val currentDay = format.format(date)

                    val today = format.format(Date())
                    var key : String? = null
                    if (currentDay.toInt() == today.toInt()) {
                        key = "today"
                    } else if (currentDay.toInt() == today.toInt() - 1) {
                        key = "yesterday"
                    } else {
                        key = "otherDay"
                    }

                    val log = CallLog(
                        it.getString(numberIndex),
                        it.getString(dateIndex).getReadableDate(),
                        it.getInt(typeIndex).getCallType()
                    )
                    if (callLogs[key] != null) {
                        (callLogs[key] as ArrayList<CallLog>).add(log)
                    } else {
                        callLogs[key] = mutableListOf(log)
                    }
                }
            }
            callback(callLogs)
        }
    }
}
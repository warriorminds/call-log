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
import javax.inject.Inject

class CallLogRepositoryImpl @Inject constructor(private val context: Context) : CallLogRepository {
    override fun getCallLogs(callback: (List<CallLog>) -> Unit) {
        val callLogs = mutableListOf<CallLog>()

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
                    val log = CallLog(
                        it.getString(numberIndex),
                        it.getString(dateIndex).getReadableDate(),
                        it.getInt(typeIndex).getCallType()
                    )
                    callLogs.add(log)
                }
            }
            callback(callLogs)
        }
    }
}
package com.hiya.calllog.receivers

import android.app.Service
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.telephony.TelephonyManager
import com.hiya.calllog.adapters.CallLogAdapter
import com.hiya.calllog.models.CallLog
import com.hiya.calllog.utils.getCallType
import com.hiya.calllog.utils.getReadableDate
import dagger.android.AndroidInjection
import java.util.*
import javax.inject.Inject

class CallReceiver @Inject constructor() : BroadcastReceiver() {

    @Inject
    lateinit var adapter: CallLogAdapter

    override fun onReceive(context: Context, intent: Intent) {
        AndroidInjection.inject(this, context)
        if (intent.action == Intent.ACTION_NEW_OUTGOING_CALL) {
            adapter.addNewCall(CallLog(
                intent.getStringExtra(Intent.EXTRA_PHONE_NUMBER),
                Date().time.getReadableDate(),
                android.provider.CallLog.Calls.OUTGOING_TYPE.getCallType()
            ))
        } else {
            val telephonyManager = context.getSystemService(Service.TELEPHONY_SERVICE) as TelephonyManager
            when (telephonyManager.callState) {
                TelephonyManager.CALL_STATE_RINGING -> {
                    adapter.addNewCall(CallLog(
                        intent.getStringExtra("incoming_number"),
                        Date().time.getReadableDate(),
                        android.provider.CallLog.Calls.INCOMING_TYPE.getCallType()
                    ))
                }
            }
        }

    }
}
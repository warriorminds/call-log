package com.hiya.calllog.repositories

import com.hiya.calllog.models.CallLog

interface CallLogRepository {
    fun  getCallLogs(callback: (MutableMap<String, List<CallLog>>) -> Unit)
}
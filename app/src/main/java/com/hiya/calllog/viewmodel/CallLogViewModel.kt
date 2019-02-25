package com.hiya.calllog.viewmodel

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.hiya.calllog.models.CallLog
import com.hiya.calllog.repositories.CallLogRepository
import javax.inject.Inject

class CallLogViewModel @Inject constructor(private val repository: CallLogRepository) : ViewModel() {

    private var logs: MutableLiveData<List<CallLog>> = MutableLiveData()

    fun getCallLog(): MutableLiveData<List<CallLog>> {
        repository.getCallLogs { results(it) }
        return logs
    }

    fun results() = logs

    private fun results(results: List<CallLog>) {
        logs.value = results
    }
}
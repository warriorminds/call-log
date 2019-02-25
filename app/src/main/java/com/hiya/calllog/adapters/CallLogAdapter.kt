package com.hiya.calllog.adapters

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.hiya.calllog.R
import com.hiya.calllog.models.CallLog
import kotlinx.android.synthetic.main.log_item.view.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CallLogAdapter @Inject constructor() : RecyclerView.Adapter<CallLogAdapter.ViewHolder>() {

    private var callLogs = mutableListOf<CallLog>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(parent)

    override fun getItemCount() = callLogs.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val log = callLogs[position]
        holder.itemView.tv_phone.text = log.phoneNumber
        holder.itemView.tv_call_type.text = log.type
        holder.itemView.tv_date.text = log.date
    }

    fun setCallLogs(callLogs: List<CallLog>) {
        this.callLogs = callLogs.toMutableList()
        notifyDataSetChanged()
    }

    fun addNewCall(callLog: CallLog) {
        val size = callLogs.size
        this.callLogs.add(0, callLog)
        if (size == 50) {
            this.callLogs.removeAt(50)
        }
        notifyItemRangeInserted(0, 1)
        notifyItemRangeRemoved(50, 1)
    }

    fun clear() {
        val size = callLogs.size
        callLogs.clear()
        notifyItemRangeRemoved(0, size - 1)
    }

    class ViewHolder(viewGroup: ViewGroup) : RecyclerView.ViewHolder(
        LayoutInflater.from(viewGroup.context).inflate(R.layout.log_item, viewGroup, false)
    )
}
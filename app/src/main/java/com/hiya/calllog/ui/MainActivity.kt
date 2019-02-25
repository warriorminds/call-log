package com.hiya.calllog.ui

import android.Manifest
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.support.design.widget.Snackbar
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.telephony.TelephonyManager
import com.hiya.calllog.R
import com.hiya.calllog.adapters.CallLogAdapter
import com.hiya.calllog.receivers.CallReceiver
import com.hiya.calllog.viewmodel.CallLogViewModel
import dagger.android.AndroidInjection
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject

class MainActivity : AppCompatActivity(), SwipeRefreshLayout.OnRefreshListener {

    @Inject
    lateinit var viewModel: CallLogViewModel
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    @Inject
    lateinit var callLogAdapter: CallLogAdapter
    @Inject
    lateinit var callReceiver: CallReceiver
    private lateinit var manager: LinearLayoutManager

    companion object {
        private const val CALL_LOG_PERMISSION_REQUEST = 1000
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        refresh_logs.setOnRefreshListener(this)

        viewModel = ViewModelProviders.of(this, viewModelFactory).get(CallLogViewModel::class.java)
        manager = LinearLayoutManager(this)
        rv_call_log.apply {
            val itemDecorator = DividerItemDecoration(this@MainActivity, manager.orientation)
            removeItemDecoration(itemDecorator)
            setHasFixedSize(true)
            layoutManager = manager
            adapter = callLogAdapter
            addItemDecoration(itemDecorator)
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.results().observe(this, Observer {
            refresh_logs.isRefreshing = false
            callLogAdapter.clear()
            callLogAdapter.setCallLogs(it!!)
        })
        checkPermissions()
        registerReceiver(callReceiver, IntentFilter(Intent.ACTION_NEW_OUTGOING_CALL))
        registerReceiver(callReceiver, IntentFilter(TelephonyManager.ACTION_PHONE_STATE_CHANGED))
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            CALL_LOG_PERMISSION_REQUEST -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    viewModel.getCallLog()
                } else {
                    Snackbar.make(rv_call_log, getString(R.string.permissions_denied), Snackbar.LENGTH_INDEFINITE)
                        .setAction(getString(R.string.settings)) {
                            val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                            val uri = Uri.fromParts("package", packageName, null)
                            intent.data = uri
                            startActivity(intent)
                        }.show()
                }
            }
        }
    }

    override fun onStop() {
        viewModel.results().removeObservers(this)
        unregisterReceiver(callReceiver)
        super.onStop()
    }

    override fun onRefresh() {
        viewModel.getCallLog()
    }

    private fun checkPermissions() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CALL_LOG) != PackageManager.PERMISSION_GRANTED ||
            ContextCompat.checkSelfPermission(this, Manifest.permission.PROCESS_OUTGOING_CALLS) != PackageManager.PERMISSION_GRANTED ||
            ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.READ_CALL_LOG,
                    Manifest.permission.PROCESS_OUTGOING_CALLS,
                    Manifest.permission.READ_PHONE_STATE),
                CALL_LOG_PERMISSION_REQUEST
            )
        } else {
            viewModel.getCallLog()
        }
    }
}

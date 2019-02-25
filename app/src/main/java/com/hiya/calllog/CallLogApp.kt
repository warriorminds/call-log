package com.hiya.calllog

import android.app.Activity
import android.app.Application
import android.content.BroadcastReceiver
import com.hiya.calllog.di.components.DaggerAppComponent
import com.hiya.calllog.di.modules.CallLogModule
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import dagger.android.HasBroadcastReceiverInjector
import javax.inject.Inject

class CallLogApp : Application(), HasActivityInjector, HasBroadcastReceiverInjector {

    @Inject
    lateinit var activityInjector: DispatchingAndroidInjector<Activity>
    @Inject
    lateinit var broadcastInjector: DispatchingAndroidInjector<BroadcastReceiver>

    override fun onCreate() {
        super.onCreate()
        val appComponent = DaggerAppComponent.builder()
            .callLogModule(CallLogModule(applicationContext)).build()
        appComponent.inject(this)
    }

    override fun activityInjector() = activityInjector

    override fun broadcastReceiverInjector() = broadcastInjector
}
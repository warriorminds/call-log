package com.hiya.calllog.di.modules

import com.hiya.calllog.receivers.CallReceiver
import com.hiya.calllog.ui.MainActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivitiesModule {

    @ContributesAndroidInjector
    abstract fun mainActiivty(): MainActivity

    @ContributesAndroidInjector
    abstract fun callReceiver(): CallReceiver
}
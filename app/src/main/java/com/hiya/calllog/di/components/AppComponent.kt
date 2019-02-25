package com.hiya.calllog.di.components

import com.hiya.calllog.CallLogApp
import com.hiya.calllog.di.modules.ActivitiesModule
import com.hiya.calllog.di.modules.CallLogModule
import com.hiya.calllog.di.modules.ViewModelsModule
import dagger.Component
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

@Singleton
@Component(modules = [ AndroidSupportInjectionModule::class, ActivitiesModule::class, ViewModelsModule::class, CallLogModule::class])
interface AppComponent {
    fun inject(app: CallLogApp)
}
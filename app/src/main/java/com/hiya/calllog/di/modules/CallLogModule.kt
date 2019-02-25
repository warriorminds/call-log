package com.hiya.calllog.di.modules

import android.content.Context
import com.hiya.calllog.repositories.CallLogRepository
import com.hiya.calllog.repositories.CallLogRepositoryImpl
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class CallLogModule(val context: Context) {

    @Provides
    @Singleton
    fun providesContext(): Context = context

    @Provides
    @Singleton
    fun providesCallLogRepository(context: Context): CallLogRepository = CallLogRepositoryImpl(context)
}
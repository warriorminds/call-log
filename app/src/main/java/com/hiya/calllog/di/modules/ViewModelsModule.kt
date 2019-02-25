package com.hiya.calllog.di.modules

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.hiya.calllog.viewmodel.CallLogViewModel
import com.hiya.calllog.viewmodel.ViewModelFactory
import com.hiya.calllog.viewmodel.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ViewModelsModule {

    @Binds
    @IntoMap
    @ViewModelKey(CallLogViewModel::class)
    abstract fun bindCallLogViewModel(callLogViewModel: CallLogViewModel): ViewModel

    @Binds
    abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory
}
package com.example.cryptoapp.di

import android.content.Context
import com.example.cryptoapp.di.annotations.ApplicationScope
import com.example.cryptoapp.di.modules.DataModule
import com.example.cryptoapp.di.modules.ViewModelModule
import com.example.cryptoapp.di.modules.WorkerModule
import com.example.cryptoapp.presentation.CoinApplication
import dagger.BindsInstance
import dagger.Component

@ApplicationScope
@Component(modules = [DataModule::class, ViewModelModule::class, WorkerModule::class])
interface ApplicationComponent {

    fun inject(app: CoinApplication)

    fun activitySubcomponent(): MainActivitySubcomponent.MainActivitySubcomponentBuilder

    @Component.Builder
    interface ApplicationComponentBuilder {

        @BindsInstance
        fun context(context: Context) : ApplicationComponentBuilder

        fun build(): ApplicationComponent
    }
}
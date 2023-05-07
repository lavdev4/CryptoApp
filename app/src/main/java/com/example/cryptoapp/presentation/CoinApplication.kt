package com.example.cryptoapp.presentation

import android.app.Application
import com.example.cryptoapp.di.ApplicationComponent
import com.example.cryptoapp.di.DaggerApplicationComponent
import com.example.cryptoapp.di.annotations.ApplicationScope

@ApplicationScope
class CoinApplication : Application() {

    lateinit var applicationComponent: ApplicationComponent

    override fun onCreate() {
        super.onCreate()
        applicationComponent = DaggerApplicationComponent.builder()
            .context(applicationContext)
            .build()
    }
}
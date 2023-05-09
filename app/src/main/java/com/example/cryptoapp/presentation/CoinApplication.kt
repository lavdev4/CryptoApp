package com.example.cryptoapp.presentation

import android.app.Application
import androidx.work.Configuration
import com.example.cryptoapp.data.network.AppWorkerFactory
import com.example.cryptoapp.di.ApplicationComponent
import com.example.cryptoapp.di.DaggerApplicationComponent
import javax.inject.Inject

class CoinApplication : Application(), Configuration.Provider {

    @Inject
    lateinit var appWorkerFactory: AppWorkerFactory
    lateinit var applicationComponent: ApplicationComponent

    override fun onCreate() {
        applicationComponent = DaggerApplicationComponent.builder()
            .context(applicationContext)
            .build()
        applicationComponent.inject(this)
        super.onCreate()
    }

    override fun getWorkManagerConfiguration(): Configuration {
        return Configuration.Builder()
            .setWorkerFactory(appWorkerFactory)
            .build()
    }
}
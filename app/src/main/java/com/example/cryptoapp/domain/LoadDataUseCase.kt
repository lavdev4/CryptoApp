package com.example.cryptoapp.domain

import android.app.Application
import com.example.cryptoapp.data.RepositoryImpl

class LoadDataUseCase(private val application: Application) {

    fun loadData() {
        RepositoryImpl.getInstance(application).loadData()
    }
}
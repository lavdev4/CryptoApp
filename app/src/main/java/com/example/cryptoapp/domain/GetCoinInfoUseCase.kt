package com.example.cryptoapp.domain

import android.app.Application
import com.example.cryptoapp.data.RepositoryImpl

class GetCoinInfoUseCase(var application: Application) {

    fun getCoinInfo() {
        RepositoryImpl.getInstance(application).getCoinInfo()
    }
}
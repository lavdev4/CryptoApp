package com.example.cryptoapp.domain

import android.app.Application
import com.example.cryptoapp.data.RepositoryImpl

class GetCoinInfoListUseCase(val application: Application) {

    fun getCoinInfoList() {
        RepositoryImpl.getInstance(application).getCoinInfoList()
    }
}
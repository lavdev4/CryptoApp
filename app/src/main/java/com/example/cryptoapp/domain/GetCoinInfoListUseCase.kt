package com.example.cryptoapp.domain

import android.app.Application
import androidx.lifecycle.LiveData
import com.example.cryptoapp.data.RepositoryImpl

class GetCoinInfoListUseCase(private val application: Application) {

    fun getCoinInfoList(): LiveData<List<CoinInfoEntity>> {
        return RepositoryImpl.getInstance(application).getCoinInfoList()
    }
}
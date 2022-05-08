package com.example.cryptoapp.domain

import android.app.Application
import androidx.lifecycle.LiveData
import com.example.cryptoapp.data.RepositoryImpl

class GetCoinInfoUseCase(private var application: Application) {

    fun getCoinInfo(fSym: String): LiveData<CoinInfoEntity> {
        return RepositoryImpl.getInstance(application).getCoinInfo(fSym)
    }
}
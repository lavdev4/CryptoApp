package com.example.cryptoapp.domain

import android.app.Application
import androidx.lifecycle.LiveData
import com.example.cryptoapp.data.CoinPriceInfo
import com.example.cryptoapp.data.RepositoryImpl

class GetPriceInfoAboutCoinUseCase {
    fun getPriceInfoAboutCoin(application: Application, fSym: String): LiveData<CoinPriceInfo> {
        return RepositoryImpl.getInstance(application).getPriceInfoAboutCoin(fSym)
    }
}
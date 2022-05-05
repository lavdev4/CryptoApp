package com.example.cryptoapp.domain

import android.app.Application
import com.example.cryptoapp.data.CoinPriceInfo
import com.example.cryptoapp.data.RepositoryImpl

class InsertPriceListUseCase {
    fun insertPriceList(application: Application, priceList: List<CoinPriceInfo>) {
        RepositoryImpl.getInstance(application).insertPriceList(priceList)
    }
}
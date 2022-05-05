package com.example.cryptoapp.domain

import android.app.Application
import androidx.lifecycle.LiveData
import com.example.cryptoapp.data.CoinPriceInfo
import com.example.cryptoapp.data.RepositoryImpl

class GetPriceListUseCase {
    fun getPriceList(application: Application): LiveData<List<CoinPriceInfo>> {
        return RepositoryImpl.getInstance(application).getPriceList()
    }
}
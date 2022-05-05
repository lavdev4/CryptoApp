package com.example.cryptoapp.domain

import android.app.Application
import com.example.cryptoapp.data.RepositoryImpl
import com.example.cryptoapp.data.network.CoinPriceInfoRawData
import io.reactivex.Single

class GetFullPriceListUseCase {
    fun getFullPriceList(
        application: Application,
        apiKey: String,
        fSyms: String,
        tSyms: String
    ): Single<CoinPriceInfoRawData> {
        return RepositoryImpl.getInstance(application).getFullPriceList(apiKey, fSyms, tSyms)
    }
}
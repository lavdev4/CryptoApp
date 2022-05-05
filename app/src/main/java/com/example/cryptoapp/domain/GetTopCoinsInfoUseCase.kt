package com.example.cryptoapp.domain

import android.app.Application
import com.example.cryptoapp.data.RepositoryImpl
import com.example.cryptoapp.data.network.CoinInfoListOfData
import io.reactivex.Single

class GetTopCoinsInfoUseCase {
    fun getTopCoinsInfo(
        application: Application,
        apiKey: String,
        limit: Int,
        tSym: String
    ): Single<CoinInfoListOfData> {
        return RepositoryImpl.getInstance(application).getTopCoinsInfo(apiKey, limit, tSym)
    }
}
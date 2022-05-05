package com.example.cryptoapp.domain

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import com.example.cryptoapp.data.CoinPriceInfo
import com.example.cryptoapp.data.network.ApiService
import com.example.cryptoapp.data.network.CoinInfoListOfData
import com.example.cryptoapp.data.network.CoinPriceInfoRawData
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface Repository {

    fun insertPriceList(priceList: List<CoinPriceInfo>)

    fun getPriceList(): LiveData<List<CoinPriceInfo>>

    fun getTopCoinsInfo(apiKey: String, limit: Int, tSym: String): Single<CoinInfoListOfData>

    fun getPriceInfoAboutCoin(fSym: String): LiveData<CoinPriceInfo>

    fun getFullPriceList(apiKey: String, fSyms: String, tSyms: String): Single<CoinPriceInfoRawData>
}
package com.example.cryptoapp.data

import android.app.Application
import androidx.lifecycle.LiveData
import com.example.cryptoapp.data.database.AppDatabase
import com.example.cryptoapp.data.network.ApiFactory
import com.example.cryptoapp.data.network.CoinInfoListOfData
import com.example.cryptoapp.data.network.CoinPriceInfoRawData
import com.example.cryptoapp.domain.Repository
import io.reactivex.Single

class RepositoryImpl private constructor(application: Application) : Repository {

    private val apiService = ApiFactory.apiService
    private val dao = AppDatabase.getInstance(application).coinPriceInfoDao()

    companion object {
        private val repository: RepositoryImpl? = null
        private val LOCK = Any()

        fun getInstance(application: Application): RepositoryImpl {
            synchronized(LOCK) {
                repository?.let { return it }
                return RepositoryImpl(application)
            }
        }
    }

    override fun getTopCoinsInfo(
        apiKey: String,
        limit: Int,
        tSym: String
    ): Single<CoinInfoListOfData> {
        return apiService.getTopCoinsInfo(apiKey, limit, tSym)
    }

    override fun getFullPriceList(
        apiKey: String,
        fSyms: String,
        tSyms: String
    ): Single<CoinPriceInfoRawData> {
        return apiService.getFullPriceList(apiKey, fSyms, tSyms)
    }

    override fun insertPriceList(priceList: List<CoinPriceInfo>) {
        return dao.insertPriceList(priceList)
    }

    override fun getPriceList(): LiveData<List<CoinPriceInfo>> {
       return dao.getPriceList()
    }

    override fun getPriceInfoAboutCoin(fSym: String): LiveData<CoinPriceInfo> {
        return dao.getPriceInfoAboutCoin(fSym)
    }
}
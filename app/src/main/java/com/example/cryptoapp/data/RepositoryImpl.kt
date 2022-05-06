package com.example.cryptoapp.data

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.example.cryptoapp.data.database.AppDatabase
import com.example.cryptoapp.data.network.ApiFactory
import com.example.cryptoapp.domain.CoinInfoEntity
import com.example.cryptoapp.domain.Repository

class RepositoryImpl private constructor(application: Application) : Repository {

    private val apiService = ApiFactory.apiService
    private val dao = AppDatabase.getInstance(application).coinInfoDao()
    private val coinMapper = CoinMapper()

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

    override fun getCoinInfo(fSym: String): LiveData<CoinInfoEntity> {
        return Transformations.map(dao.getCoinInfo(fSym)) {
            coinMapper.mapCoinInfoDbModelToCoinInfoEntity(it)
        }
    }

    override fun getCoinInfoList(): LiveData<List<CoinInfoEntity>> {
        return Transformations.map(dao.getCoinInfoList()) { coinInfoList ->
            coinInfoList.map { coinMapper.mapCoinInfoDbModelToCoinInfoEntity(it) }
        }
    }

    override fun loadData() {

    }
}
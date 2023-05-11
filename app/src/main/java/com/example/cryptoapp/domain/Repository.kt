package com.example.cryptoapp.domain

import androidx.lifecycle.LiveData
import com.example.cryptoapp.domain.entities.CoinInfoEntity

interface Repository {
    fun getCoinInfo(currencyName: String): LiveData<CoinInfoEntity>
    fun getCoinInfoList(): LiveData<List<CoinInfoEntity>>
    fun loadData()
}
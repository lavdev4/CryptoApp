package com.example.cryptoapp.domain

import androidx.lifecycle.LiveData

interface Repository {
    fun getCoinInfo(fSym: String): LiveData<CoinInfoEntity>
    fun getCoinInfoList(): LiveData<List<CoinInfoEntity>>
    fun loadData()
}
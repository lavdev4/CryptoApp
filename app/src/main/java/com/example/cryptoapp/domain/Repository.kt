package com.example.cryptoapp.domain

import androidx.lifecycle.LiveData
import androidx.paging.PagingData
import com.example.cryptoapp.domain.entities.CoinInfoEntity
import kotlinx.coroutines.flow.Flow

interface Repository {
    fun getCoinInfo(coinName: String): LiveData<CoinInfoEntity>
    fun getCoinInfoList(): Flow<PagingData<CoinInfoEntity>>
    fun loadData()
}
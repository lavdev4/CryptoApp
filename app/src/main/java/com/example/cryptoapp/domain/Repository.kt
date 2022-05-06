package com.example.cryptoapp.domain

import androidx.lifecycle.LiveData
import com.example.cryptoapp.data.network.model.CoinInfoDto
import com.example.cryptoapp.data.network.model.CoinNamesListDto
import com.example.cryptoapp.data.network.model.CoinInfoJsonContainerDto
import io.reactivex.Single

interface Repository {
    fun getCoinInfo(fSym: String): LiveData<CoinInfoEntity>
    fun getCoinInfoList(): LiveData<List<CoinInfoEntity>>
    fun loadData()
}
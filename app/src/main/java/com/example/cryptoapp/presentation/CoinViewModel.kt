package com.example.cryptoapp.presentation

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.cryptoapp.domain.CoinInfoEntity
import com.example.cryptoapp.domain.GetCoinInfoListUseCase
import com.example.cryptoapp.domain.GetCoinInfoUseCase
import com.example.cryptoapp.domain.LoadDataUseCase

class CoinViewModel(private val app: Application) : AndroidViewModel(app) {

    //LiveData. Список
    val priceList = GetCoinInfoListUseCase(app).getCoinInfoList()

    fun getDetailInfo(fSym: String): LiveData<CoinInfoEntity> {
        return GetCoinInfoUseCase(app).getCoinInfo(fSym)
    }

    init {
        LoadDataUseCase(app).loadData()
    }
}
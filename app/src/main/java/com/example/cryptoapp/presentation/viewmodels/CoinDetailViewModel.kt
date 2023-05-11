package com.example.cryptoapp.presentation.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.cryptoapp.domain.entities.CoinInfoEntity
import com.example.cryptoapp.domain.usecases.GetCoinInfoUseCase
import javax.inject.Inject

class CoinDetailViewModel @Inject constructor(
    private val getCoinInfoUseCase: GetCoinInfoUseCase
) : ViewModel() {

    fun getDetailInfo(coinName: String): LiveData<CoinInfoEntity> {
        return getCoinInfoUseCase(coinName)
    }
}
package com.example.cryptoapp.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.cryptoapp.domain.CoinInfoEntity
import com.example.cryptoapp.domain.GetCoinInfoUseCase
import javax.inject.Inject

class CoinDetailViewModel @Inject constructor(
    private val getCoinInfoUseCase: GetCoinInfoUseCase
) : ViewModel() {

    fun getDetailInfo(fSym: String): LiveData<CoinInfoEntity> {
        return getCoinInfoUseCase(fSym)
    }
}
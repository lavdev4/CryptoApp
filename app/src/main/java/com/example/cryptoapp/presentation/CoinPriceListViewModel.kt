package com.example.cryptoapp.presentation

import androidx.lifecycle.ViewModel
import com.example.cryptoapp.domain.GetCoinInfoListUseCase
import com.example.cryptoapp.domain.LoadDataUseCase
import javax.inject.Inject

class CoinPriceListViewModel @Inject constructor(
    getCoinInfoListUseCase: GetCoinInfoListUseCase,
    loadDataUseCase: LoadDataUseCase
) : ViewModel() {

    val priceList = getCoinInfoListUseCase()

    init {
        loadDataUseCase()
    }
}
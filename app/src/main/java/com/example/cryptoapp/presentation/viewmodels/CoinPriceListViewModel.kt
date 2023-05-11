package com.example.cryptoapp.presentation.viewmodels

import androidx.lifecycle.ViewModel
import com.example.cryptoapp.domain.usecases.GetCoinInfoListUseCase
import com.example.cryptoapp.domain.usecases.LoadDataUseCase
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
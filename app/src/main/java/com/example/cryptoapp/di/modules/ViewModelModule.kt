package com.example.cryptoapp.di.modules

import androidx.lifecycle.ViewModel
import com.example.cryptoapp.di.annotations.ViewModelKey
import com.example.cryptoapp.presentation.viewmodels.CoinDetailViewModel
import com.example.cryptoapp.presentation.viewmodels.CoinPriceListViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ViewModelModule {

    @IntoMap
    @ViewModelKey(CoinPriceListViewModel::class)
    @Binds
    abstract fun bindCoinPriceListViewModel(impl: CoinPriceListViewModel): ViewModel

    @IntoMap
    @ViewModelKey(CoinDetailViewModel::class)
    @Binds
    abstract fun bindCoinDetailViewModel(impl: CoinDetailViewModel): ViewModel
}
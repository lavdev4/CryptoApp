package com.example.cryptoapp.di

import com.example.cryptoapp.di.annotations.MainActivityScope
import com.example.cryptoapp.presentation.fragments.CoinDetailFragment
import com.example.cryptoapp.presentation.fragments.CoinPriceListFragment
import dagger.Subcomponent

@MainActivityScope
@Subcomponent
interface MainActivitySubcomponent {

    fun inject(impl: CoinPriceListFragment)
    fun inject(impl: CoinDetailFragment)

    @Subcomponent.Builder
    interface MainActivitySubcomponentBuilder {

        fun build(): MainActivitySubcomponent
    }
}
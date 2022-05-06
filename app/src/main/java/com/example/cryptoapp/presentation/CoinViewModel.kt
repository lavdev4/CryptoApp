package com.example.cryptoapp.presentation

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.cryptoapp.data.database.CoinInfoDbModel
import com.example.cryptoapp.data.network.ApiFactory
import com.example.cryptoapp.data.network.model.CoinInfoDto
import com.example.cryptoapp.data.network.model.CoinInfoJsonContainerDto
import com.example.cryptoapp.domain.CoinInfoEntity
import com.example.cryptoapp.domain.GetCoinInfoListUseCase
import com.example.cryptoapp.domain.GetCoinInfoUseCase
import com.example.cryptoapp.domain.LoadDataUseCase
import com.google.gson.Gson
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit

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
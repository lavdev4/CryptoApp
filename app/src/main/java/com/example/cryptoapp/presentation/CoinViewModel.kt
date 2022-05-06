package com.example.cryptoapp.presentation

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.cryptoapp.data.network.ApiFactory
import com.example.cryptoapp.data.network.model.CoinInfoDto
import com.example.cryptoapp.data.network.model.CoinInfoJsonContainerDto
import com.google.gson.Gson
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit

class CoinViewModel() : ViewModel() {

    private val compositeDisposable = CompositeDisposable()

    val priceList = //usecase

    fun getDetailInfo(fSym: String): LiveData<CoinInfoDto> {
        //usecase
    }

    init {
        //usecase
    }

    private fun loadData() {
        val disposable = ApiFactory.apiService.getTopCoinsInfo(limit = 50)
            //преобразование коллекции в одну строку со всем списком валют через ","
            .map { it.coinNameContainers?.map { it.coinName?.names }?.joinToString(",") }
            //получаем json по списку валют сформированному выше
            .flatMap { ApiFactory.apiService.getFullPriceList(fSyms = it) }
            //парсим полученный json и получаем список объектов
            .map { getPriceListFromRawData(it) }
            //задержка перед подпиской
            .delaySubscription(10, TimeUnit.SECONDS)
            //повтор подписки заново
            .repeat()
            //возвращает Flowable который переподписывается при сработке onError()
            .retry()
            .subscribeOn(Schedulers.io())
            .subscribe({
                db.coinPriceInfoDao().insertPriceList(it)
                Log.d("TEST_OF_LOADING_DATA", "Success: $it")
            }, {
                Log.d("TEST_OF_LOADING_DATA", "Failure: ${it.message}")
            })
        compositeDisposable.add(disposable)
    }

    private fun getPriceListFromRawData(
        coinInfoJsonContainerDto: CoinInfoJsonContainerDto
    ): List<CoinInfoDto> {
        val result = ArrayList<CoinInfoDto>()
        val jsonObject = coinInfoJsonContainerDto.json ?: return result
        val coinKeySet = jsonObject.keySet()
        for (coinKey in coinKeySet) {
            val currencyJson = jsonObject.getAsJsonObject(coinKey)
            val currencyKeySet = currencyJson.keySet()
            for (currencyKey in currencyKeySet) {
                val priceInfo = Gson().fromJson(
                    currencyJson.getAsJsonObject(currencyKey),
                    CoinInfoDto::class.java
                )
                result.add(priceInfo)
            }
        }
        return result
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }
}
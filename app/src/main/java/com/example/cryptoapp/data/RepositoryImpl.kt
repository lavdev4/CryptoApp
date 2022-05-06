package com.example.cryptoapp.data

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.example.cryptoapp.data.database.AppDatabase
import com.example.cryptoapp.data.network.ApiFactory
import com.example.cryptoapp.domain.CoinInfoEntity
import com.example.cryptoapp.domain.Repository
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit

class RepositoryImpl private constructor(application: Application) : Repository {

    private val apiService = ApiFactory.apiService
    private val dao = AppDatabase.getInstance(application).coinInfoDao()
    private val coinMapper = CoinMapper()

    companion object {
        private val repository: RepositoryImpl? = null
        private val LOCK = Any()

        fun getInstance(application: Application): RepositoryImpl {
            synchronized(LOCK) {
                repository?.let { return it }
                return RepositoryImpl(application)
            }
        }
    }

    override fun getCoinInfo(fSym: String): LiveData<CoinInfoEntity> {
        return Transformations.map(dao.getCoinInfo(fSym)) {
            coinMapper.mapCoinInfoDbModelToEntity(it)
        }
    }

    override fun getCoinInfoList(): LiveData<List<CoinInfoEntity>> {
        return Transformations.map(dao.getCoinInfoList()) { coinInfoList ->
            coinInfoList.map { coinMapper.mapCoinInfoDbModelToEntity(it) }
        }
    }

    override fun loadData() {
        val disposable = ApiFactory.apiService.getTopCoinsInfo(limit = 50)
            //преобразование коллекции в одну строку со всем списком валют через ","
            .map { coinNamesList -> coinNamesList.coinNameContainers?.map {
                    it.coinName?.name
                }?.joinToString(",")
            }
            //получаем json по списку валют сформированному выше
            .flatMap { ApiFactory.apiService.getFullPriceList(fSyms = it) }
            //парсим полученный json и получаем список объектов
            .map { coinMapper.mapCoinInfoJsonToEntity(it) }
            //задержка перед подпиской
            .delaySubscription(10, TimeUnit.SECONDS)
            //повтор подписки заново
            .repeat()
            //возвращает Flowable который переподписывается при сработке onError()
            .retry()
            .subscribeOn(Schedulers.io())
            .subscribe({
                dao.insertCoinInfoList(coinMapper.mapCoinInfoDtoToDbModel(it))
                Log.d("TEST_OF_LOADING_DATA", "Success: $it")
            }, {
                Log.d("TEST_OF_LOADING_DATA", "Failure: ${it.message}")
            })
    }
}
package com.example.cryptoapp.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import androidx.work.*
import com.example.cryptoapp.data.database.CoinInfoDao
import com.example.cryptoapp.data.mappers.CoinMapper
import com.example.cryptoapp.data.workers.LoadDataWorker
import com.example.cryptoapp.di.annotations.ApplicationScope
import com.example.cryptoapp.domain.Repository
import com.example.cryptoapp.domain.entities.CoinInfoEntity
import javax.inject.Inject

@ApplicationScope
class RepositoryImpl @Inject constructor(
    private val dao: CoinInfoDao,
    private val workManager: WorkManager,
    private val coinMapper: CoinMapper
) : Repository {

    override fun getCoinInfo(coinName: String): LiveData<CoinInfoEntity> {
        return dao.getCoinInfo(coinName).map {
            coinMapper.mapCoinInfoDbModelToEntity(it)
        }
    }

    override fun getCoinInfoList(): LiveData<List<CoinInfoEntity>> {
        return dao.getCoinInfoList().map { coinInfoList ->
            coinInfoList.map { coinMapper.mapCoinInfoDbModelToEntity(it) }
        }
    }

    override fun loadData() {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()
        val workRequest = OneTimeWorkRequest.Builder(LoadDataWorker::class.java)
            .setConstraints(constraints)
            .build()
        workManager.enqueueUniqueWork(
            LoadDataWorker.WORKER_TAG,
            ExistingWorkPolicy.REPLACE,
            workRequest)
    }
}
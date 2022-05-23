package com.example.cryptoapp.data

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.work.*
import com.example.cryptoapp.data.database.AppDatabase
import com.example.cryptoapp.data.network.ApiFactory
import com.example.cryptoapp.data.network.LoadDataWorker
import com.example.cryptoapp.domain.CoinInfoEntity
import com.example.cryptoapp.domain.Repository
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.*
import java.util.concurrent.TimeUnit

class RepositoryImpl private constructor(private val application: Application) : Repository {

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

    override suspend fun loadData() {
        val workManager = WorkManager.getInstance(application)
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()
        val workRequest = OneTimeWorkRequest.Builder(LoadDataWorker::class.java)
            .setConstraints(constraints)
            .build()
        workManager.enqueueUniqueWork(
            LoadDataWorker.workerTag,
            ExistingWorkPolicy.REPLACE,
            workRequest)
    }
}
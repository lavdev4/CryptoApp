package com.example.cryptoapp.data.network

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.ListenableWorker
import androidx.work.WorkerParameters
import com.example.cryptoapp.data.CoinMapper
import com.example.cryptoapp.data.database.CoinInfoDao
import kotlinx.coroutines.delay
import javax.inject.Inject

class LoadDataWorker private constructor(
    applicationContext: Context,
    workerParams: WorkerParameters,
    private val dao: CoinInfoDao,
    private val apiService: ApiService,
    private val mapper: CoinMapper
) : CoroutineWorker(applicationContext, workerParams) {

    companion object {
        const val workerTag = "LoadDataWorker"
    }

    override suspend fun doWork(): Result {
        Log.d("Worker hash code: ", this.id.toString())
        Log.d("Run attempt count - ", runAttemptCount.toString())
        while (true) {
            try {
                apiService.getTopCoinsInfo(limit = 50).coinNameContainers
                    ?.let { coinNameContainer ->
                        coinNameContainer.map { it.coinName?.name }.joinToString(",")
                            .let { apiService.getFullPriceList(fSyms = it) }
                            .let { mapper.mapCoinInfoJsonToEntity(it) }
                            .let { dao.insertCoinInfoList(mapper.mapCoinInfoDtoToDbModel(it)) }
                    }
                Log.d("Network request ", "succeeded.")
            } catch (exception: Exception) {
                Log.d("Network request ", "failed! /n", exception)
                Result.failure()
            }
            delay(10000)
        }
    }

    class DependencyContractFactory @Inject constructor(
        private val dao: CoinInfoDao,
        private val apiService: ApiService,
        private val mapper: CoinMapper
    ) : InnerWorkerFactory {

        override fun createWorker(
            context: Context,
            workerParameters: WorkerParameters
        ): ListenableWorker {
            return LoadDataWorker(
                context,
                workerParameters,
                dao,
                apiService,
                mapper
            )
        }
    }
}
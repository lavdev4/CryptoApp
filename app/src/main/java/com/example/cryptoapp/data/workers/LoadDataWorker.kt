package com.example.cryptoapp.data.workers

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.ListenableWorker
import androidx.work.WorkerParameters
import com.example.cryptoapp.data.database.CoinInfoDao
import com.example.cryptoapp.data.mappers.CoinMapper
import com.example.cryptoapp.data.network.ApiService
import com.example.cryptoapp.presentation.CoinApplication.Companion.LOG_DEBUG_TAG
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
        const val WORKER_TAG = "LoadDataWorker"
        const val REQUEST_REPEAT_TIMEOUT = 10000L
    }

    override suspend fun doWork(): Result {
        Log.d(LOG_DEBUG_TAG, "Work request (id: ${this.id}) run attempt #$runAttemptCount")
        while (true) {
            try {
                apiService.getTopCoinsInfo(limit = 50).coinNameContainers
                    ?.let { coinNameContainers ->
                        coinNameContainers.map { it.coinName?.name }.joinToString(",")
                            .let { apiService.getFullPriceList(fSyms = it) }
                            .let { mapper.mapCoinInfoJsonToDto(it) }
                            .let {
                                Log.d(LOG_DEBUG_TAG, "Network request succeeded with ${it.size} items.")
                                dao.insertCoinInfoList(mapper.mapCoinInfoDtoToDbModel(it))
                            }
                    }
            } catch (exception: Exception) {
                Log.d(LOG_DEBUG_TAG, "Network request failed! /n Exception: $exception")
                Result.failure()
            }
            delay(REQUEST_REPEAT_TIMEOUT)
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
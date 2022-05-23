package com.example.cryptoapp.data.network

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.cryptoapp.data.CoinMapper
import com.example.cryptoapp.data.database.AppDatabase
import kotlinx.coroutines.delay

class LoadDataWorker(
    applicationContext: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(applicationContext, workerParams) {

    companion object {
        const val workerTag = "LoadDataWorker"
    }

    private val apiService = ApiFactory.apiService
    private val dao = AppDatabase.getInstance(applicationContext).coinInfoDao()
    private val coinMapper = CoinMapper()

    override suspend fun doWork(): Result {
        Log.d("Worker hash code: ", this.id.toString())
        Log.d("Run attempt count - ", runAttemptCount.toString())
        while (true) {
            try {
                apiService.getTopCoinsInfo(limit = 50).coinNameContainers
                    ?.let { coinNameContainer ->
                        coinNameContainer.map { it.coinName?.name }.joinToString(",")
                            .let { apiService.getFullPriceList(fSyms = it) }
                            .let { coinMapper.mapCoinInfoJsonToEntity(it) }
                            .let { dao.insertCoinInfoList(coinMapper.mapCoinInfoDtoToDbModel(it)) }
                    }
                Log.d("Network request ", "succeeded.")
            } catch (exception: Exception) {
                Log.d("Network request ", "failed! /n", exception)
                Result.failure()
            }
            delay(10000)
        }
    }
}
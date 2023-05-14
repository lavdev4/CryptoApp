package com.example.cryptoapp.data

import android.util.Log
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import com.example.cryptoapp.data.database.CoinInfoDao
import com.example.cryptoapp.data.database.CoinInfoDbModel
import com.example.cryptoapp.data.mappers.CoinMapper
import com.example.cryptoapp.data.network.ApiService
import com.example.cryptoapp.presentation.CoinApplication.Companion.LOG_DEBUG_TAG
import javax.inject.Inject

@OptIn(ExperimentalPagingApi::class)
class DataRemoteMediator @Inject constructor(
    private val apiService: ApiService,
    private val dao: CoinInfoDao,
    private val mapper: CoinMapper
) : RemoteMediator<Int, CoinInfoDbModel>() {

    private var pageIndex = 0
    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, CoinInfoDbModel>
    ): MediatorResult {
        val pageSize = state.config.pageSize
        try {
            when (loadType) {
                LoadType.REFRESH -> {
                    Log.d(LOG_DEBUG_TAG, "Paging: REFRESH")
                    dao.deleteAllData()
                    pageIndex = 0
                }
                LoadType.PREPEND -> {
                    Log.d(LOG_DEBUG_TAG, "Paging: PREPEND")
                    return MediatorResult.Success(true)
                }
                LoadType.APPEND -> {
                    Log.d(LOG_DEBUG_TAG, "Paging: APPEND")
                    pageIndex++
                }
            }

            Log.d(LOG_DEBUG_TAG, "Loading page #$pageIndex")
            val loadResult = loadData(pageSize, pageIndex)
            return MediatorResult.Success(loadResult)
        } catch (exception: Exception) {
            Log.d(LOG_DEBUG_TAG, "Paging: Error")
            return MediatorResult.Error(exception)
        }
    }

    private suspend fun loadData(pageSize: Int, page: Int): Boolean {
        try {
            val data = apiService.getTopCoinsInfo(limit = pageSize, page = page).coinNameContainers
                ?.let { coinNameContainers ->
                    coinNameContainers.map { it.coinName?.name }.joinToString(",")
                        .let { apiService.getFullPriceList(fSyms = it) }
                        .let { mapper.mapCoinInfoJsonToDto(it) }
                        .let {
                            Log.d(LOG_DEBUG_TAG, "Network request succeeded with ${it.size} items.")
                            dao.insertCoinInfoList(mapper.mapCoinInfoDtoToDbModel(it))
                            it
                        }
                }
            return data?.isEmpty() ?: true
        } catch (exception: Exception) {
            Log.d(LOG_DEBUG_TAG, "Network request failed! \n Exception: $exception")
            return true
        }
    }
}
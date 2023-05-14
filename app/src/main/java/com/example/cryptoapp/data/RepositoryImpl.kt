package com.example.cryptoapp.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import androidx.paging.*
import androidx.work.*
import com.example.cryptoapp.data.database.CoinInfoDao
import com.example.cryptoapp.data.mappers.CoinMapper
import com.example.cryptoapp.data.network.ApiService
import com.example.cryptoapp.data.workers.LoadDataWorker
import com.example.cryptoapp.di.annotations.ApplicationScope
import com.example.cryptoapp.domain.Repository
import com.example.cryptoapp.domain.entities.CoinInfoEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@ApplicationScope
class RepositoryImpl @Inject constructor(
    private val dao: CoinInfoDao,
    private val workManager: WorkManager,
    private val coinMapper: CoinMapper,
    private val remoteMediator: DataRemoteMediator
) : Repository {

    override fun getCoinInfo(coinName: String): LiveData<CoinInfoEntity> {
        return dao.getCoinInfo(coinName).map {
            coinMapper.mapCoinInfoDbModelToEntity(it)
        }
    }

    @OptIn(ExperimentalPagingApi::class)
    override fun getCoinInfoList(): Flow<PagingData<CoinInfoEntity>> {
        return Pager(
            config = PagingConfig(pageSize = ApiService.PAGE_SIZE),
            remoteMediator = remoteMediator,
            pagingSourceFactory = { dao.getCoinInfoList() }
        ).flow.map { pagingData ->
            pagingData.map { coinMapper.mapCoinInfoDbModelToEntity(it) }
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
            workRequest
        )
    }
}
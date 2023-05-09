package com.example.cryptoapp.di

import com.example.cryptoapp.data.network.InnerWorkerFactory
import com.example.cryptoapp.data.network.LoadDataWorker
import com.example.cryptoapp.di.annotations.WorkerKey
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
interface WorkerModule {

    @Binds
    @IntoMap
    @WorkerKey(LoadDataWorker::class)
    fun bindLoadDataWorkerInnerFactory(factory: LoadDataWorker.DependencyContractFactory): InnerWorkerFactory
}
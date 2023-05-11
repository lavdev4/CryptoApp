package com.example.cryptoapp.di.modules

import com.example.cryptoapp.data.workers.InnerWorkerFactory
import com.example.cryptoapp.data.workers.LoadDataWorker
import com.example.cryptoapp.di.annotations.WorkerKey
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class WorkerModule {

    @Binds
    @IntoMap
    @WorkerKey(LoadDataWorker::class)
    abstract fun bindLoadDataWorkerInnerFactory(factory: LoadDataWorker.DependencyContractFactory): InnerWorkerFactory
}
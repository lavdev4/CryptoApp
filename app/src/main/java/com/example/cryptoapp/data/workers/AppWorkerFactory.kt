package com.example.cryptoapp.data.workers

import android.content.Context
import androidx.work.ListenableWorker
import androidx.work.WorkerFactory
import androidx.work.WorkerParameters
import javax.inject.Inject
import javax.inject.Provider

class AppWorkerFactory @Inject constructor(
    private val innerFactories: @JvmSuppressWildcards Map<Class<out ListenableWorker>, Provider<InnerWorkerFactory>>
): WorkerFactory() {

    override fun createWorker(
        appContext: Context,
        workerClassName: String,
        workerParameters: WorkerParameters
    ): ListenableWorker? {
        return when (workerClassName) {
            LoadDataWorker::class.qualifiedName -> {
                val innerWorkerFactory = innerFactories[LoadDataWorker::class.java]?.get()
                return innerWorkerFactory?.createWorker(appContext, workerParameters)
            }
            else -> null
        }
    }
}
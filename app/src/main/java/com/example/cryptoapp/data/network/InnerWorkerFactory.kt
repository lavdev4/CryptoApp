package com.example.cryptoapp.data.network

import android.content.Context
import androidx.work.ListenableWorker
import androidx.work.WorkerParameters

interface InnerWorkerFactory {

    fun createWorker(
        context: Context,
        workerParameters: WorkerParameters
    ): ListenableWorker
}
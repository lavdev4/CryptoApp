package com.example.cryptoapp.di

import android.content.Context
import androidx.work.WorkManager
import com.example.cryptoapp.data.RepositoryImpl
import com.example.cryptoapp.data.database.AppDatabase
import com.example.cryptoapp.data.database.CoinInfoDao
import com.example.cryptoapp.domain.Repository
import dagger.Binds
import dagger.Module
import dagger.Provides

@Module
abstract class DataModule {

    @Binds
    abstract fun bindRepository(impl: RepositoryImpl): Repository

    companion object {
        @Provides
        fun provideAppDatabase(context: Context): CoinInfoDao {
            return AppDatabase.getInstance(context).coinInfoDao()
        }

        @Provides
        fun provideWorkManager(context: Context): WorkManager {
            return WorkManager.getInstance(context)
        }
    }
}
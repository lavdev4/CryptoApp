package com.example.cryptoapp.data.database

import androidx.lifecycle.LiveData
import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface CoinInfoDao {
    @Query("SELECT * FROM full_price_list WHERE fromSymbol == :fSym LIMIT 1")
    fun getCoinInfo(fSym: String): LiveData<CoinInfoDbModel>

    @Query("SELECT * FROM full_price_list")
    fun getCoinInfoList(): PagingSource<Int, CoinInfoDbModel>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCoinInfoList(priceList: List<CoinInfoDbModel>)

    @Query("DELETE FROM full_price_list")
    suspend fun deleteAllData()
}

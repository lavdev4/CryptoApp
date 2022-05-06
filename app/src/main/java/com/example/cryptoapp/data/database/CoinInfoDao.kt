package com.example.cryptoapp.data.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.cryptoapp.data.network.model.CoinInfoDto

@Dao
interface CoinInfoDao {
    @Query("SELECT * FROM full_price_list WHERE fromSymbol == :fSym LIMIT 1")
    fun getCoinInfo(fSym: String): LiveData<CoinInfoDbModel>

    @Query("SELECT * FROM full_price_list")
    fun getCoinInfoList(): LiveData<List<CoinInfoDbModel>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertCoinInfoList(priceList: List<CoinInfoDbModel>)
}
package com.example.cryptoapp.domain

import androidx.room.PrimaryKey
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class CoinInfoEntity(
    val fromSymbol: String,
    val toSymbol: String?,
    val imageUrl: String?,
    val price: String?,
    val lastUpdate: Long?
)
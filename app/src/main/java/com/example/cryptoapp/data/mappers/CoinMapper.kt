package com.example.cryptoapp.data.mappers

import com.example.cryptoapp.data.database.CoinInfoDbModel
import com.example.cryptoapp.data.network.ApiFactory
import com.example.cryptoapp.data.network.model.CoinInfoDto
import com.example.cryptoapp.data.network.model.CoinInfoJsonContainerDto
import com.example.cryptoapp.domain.entities.CoinInfoEntity
import com.google.gson.Gson
import java.sql.Timestamp
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class CoinMapper @Inject constructor() {

    fun mapCoinInfoDbModelToEntity(coinInfoDbModel: CoinInfoDbModel): CoinInfoEntity {
        return CoinInfoEntity(
            fromSymbol = coinInfoDbModel.fromSymbol,
            toSymbol = coinInfoDbModel.toSymbol,
            price = coinInfoDbModel.price,
            lastUpdate = convertTimestampToTime(coinInfoDbModel.lastUpdate),
            highDay = coinInfoDbModel.highDay,
            lowDay = coinInfoDbModel.lowDay,
            lastMarket = coinInfoDbModel.lastMarket,
            imageUrl = convertImageUrlToFullImageUrl(coinInfoDbModel.imageUrl)
        )
    }

    fun mapCoinInfoDtoToDbModel(coinInfoDto: List<CoinInfoDto>): List<CoinInfoDbModel> {
        return coinInfoDto.map {
            CoinInfoDbModel(
                fromSymbol = it.fromSymbol,
                toSymbol = it.toSymbol,
                price = it.price,
                lastUpdate = it.lastUpdate,
                highDay = it.highDay,
                lowDay = it.lowDay,
                lastMarket = it.lastMarket,
                imageUrl = it.imageUrl
            )
        }
    }

    fun mapCoinInfoJsonToDto(
        coinInfoJsonContainerDto: CoinInfoJsonContainerDto
    ): List<CoinInfoDto> {
        val result = ArrayList<CoinInfoDto>()
        val jsonObject = coinInfoJsonContainerDto.json ?: return result
        val coinKeySet = jsonObject.keySet()
        for (coinKey in coinKeySet) {
            val currencyJson = jsonObject.getAsJsonObject(coinKey)
            val currencyKeySet = currencyJson.keySet()
            for (currencyKey in currencyKeySet) {
                val priceInfo = Gson().fromJson(
                    currencyJson.getAsJsonObject(currencyKey),
                    CoinInfoDto::class.java
                )
                result.add(priceInfo)
            }
        }
        return result
    }

    private fun convertTimestampToTime(timestamp: Long?): String {
        if (timestamp == null) return ""
        val stamp = Timestamp(timestamp * 1000)
        val date = Date(stamp.time)
        val pattern = "HH:mm:ss"
        val sdf = SimpleDateFormat(pattern, Locale.getDefault())
        sdf.timeZone = TimeZone.getDefault()
        return sdf.format(date)
    }

    private fun convertImageUrlToFullImageUrl(url: String?): String {
        return ApiFactory.BASE_IMAGE_URL + url
    }
}
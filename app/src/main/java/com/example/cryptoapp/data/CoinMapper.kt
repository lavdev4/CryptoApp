package com.example.cryptoapp.data

import com.example.cryptoapp.data.database.CoinInfoDbModel
import com.example.cryptoapp.data.network.ApiFactory
import com.example.cryptoapp.data.network.model.CoinInfoDto
import com.example.cryptoapp.data.network.model.CoinInfoJsonContainerDto
import com.example.cryptoapp.domain.CoinInfoEntity
import com.google.gson.Gson

class CoinMapper {

    fun mapCoinInfoDbModelToCoinInfoEntity(coinInfoDbModel: CoinInfoDbModel): CoinInfoEntity {
        val formatImageUrl = ApiFactory.BASE_IMAGE_URL + coinInfoDbModel.imageUrl
        return CoinInfoEntity(
            coinInfoDbModel.fromSymbol,
            coinInfoDbModel.toSymbol,
            formatImageUrl,
            coinInfoDbModel.price,
            coinInfoDbModel.lastUpdate
        )
    }


}
package com.example.cryptoapp.data.network

import com.example.cryptoapp.data.network.model.CoinInfoJsonContainerDto
import com.example.cryptoapp.data.network.model.CoinNamesListDto
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    // Get a number of top coins by their total volume across all markets in the last 24 hours.
    // Default value is first page (0) and the top 10 coins.
    // Получаем только названия валют
    @GET("top/totalvolfull")
    suspend fun getTopCoinsInfo(
        @Query(QUERY_PARAM_API_KEY) apiKey: String = "",
        @Query(QUERY_PARAM_LIMIT) limit: Int = 10,
        @Query(QUERY_PARAM_TO_SYMBOL) tSym: String = CURRENCY
    ): CoinNamesListDto

    // Get all the current trading info (price, vol, open, high, low etc) of any list
    // of cryptocurrencies in any other currency that you need.
    // If the crypto does not trade directly into the toSymbol requested,
    // BTC will be used for conversion.
    // This API also returns Display values for all the fields.
    // If the opposite pair trades we invert it (eg.: BTC-XMR)
    // Получаем информацию о каждой валюте
    @GET("pricemultifull")
    suspend fun getFullPriceList(
        @Query(QUERY_PARAM_API_KEY) apiKey: String = "",
        @Query(QUERY_PARAM_FROM_SYMBOLS) fSyms: String,
        @Query(QUERY_PARAM_TO_SYMBOLS) tSyms: String = CURRENCY
    ): CoinInfoJsonContainerDto

    companion object {
        private const val QUERY_PARAM_API_KEY = "api_key"
        // The number of coins to return in the toplist,
        // default 10, min 10, max 100 will round to steps of 10 coins
        // [ Min - 1] [ Max - 100] [ Default - 10]
        private const val QUERY_PARAM_LIMIT = "limit"
        // The currency symbol to convert into [ Min length - 1] [ Max length - 30]
        private const val QUERY_PARAM_TO_SYMBOL = "tsym"
        // Comma separated cryptocurrency symbols list to convert into
        // [ Min length - 1] [ Max length - 100]
        private const val QUERY_PARAM_TO_SYMBOLS = "tsyms"
        // Comma separated cryptocurrency symbols list [ Min length - 1] [ Max length - 1000]
        private const val QUERY_PARAM_FROM_SYMBOLS = "fsyms"

        private const val CURRENCY = "USD"
    }
}
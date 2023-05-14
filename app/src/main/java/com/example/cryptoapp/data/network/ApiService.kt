package com.example.cryptoapp.data.network

import com.example.cryptoapp.data.network.model.CoinInfoJsonContainerDto
import com.example.cryptoapp.data.network.model.CoinNamesListDto
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    /** API docs */

    // Получаем только названия валют
    /** Get a number of top coins by their total volume across all markets in the last 24 hours.
    Default value is first page (0) and the top 10 coins. */
    @GET("top/totalvolfull")
    suspend fun getTopCoinsInfo(
        @Query(QUERY_PARAM_API_KEY) apiKey: String = "",
        @Query(QUERY_PARAM_LIMIT) limit: Int = PAGE_SIZE,
        @Query(QUERY_PARAM_PAGE) page: Int = DEFAULT_PAGE,
        @Query(QUERY_PARAM_TO_SYMBOL) tSym: String = CONVERTED_IN
    ): CoinNamesListDto

    // Получаем информацию о каждой валюте
    /** Get all the current trading info (price, vol, open, high, low etc) of any list
    of cryptocurrencies in any other currency that you need.
    If the crypto does not trade directly into the toSymbol requested,
    BTC will be used for conversion.
    This API also returns Display values for all the fields.
    If the opposite pair trades we invert it (eg.: BTC-XMR) */
    @GET("pricemultifull")
    suspend fun getFullPriceList(
        @Query(QUERY_PARAM_API_KEY) apiKey: String = "",
        @Query(QUERY_PARAM_FROM_SYMBOLS) fSyms: String,
        @Query(QUERY_PARAM_TO_SYMBOLS) tSyms: String = CONVERTED_IN
    ): CoinInfoJsonContainerDto

    companion object {
        private const val QUERY_PARAM_API_KEY = "api_key"
        /** The number of coins to return in the toplist,
        default 10, min 10, max 100 will round to steps of 10 coins
        [ Min - 1] [ Max - 100] [ Default - 10] */
        private const val QUERY_PARAM_LIMIT = "limit"
        /** The pagination for the request. If you want to paginate by 50 for example,
         pass in the limit_toplist param the value 50 and increasing page_toplist integer values,
         0 would return coins 0-50, 1 returns coins 50-100 [ Min - 0] [ Default - 0] */
        private const val QUERY_PARAM_PAGE = "page"
        /** The currency symbol to convert into [ Min length - 1] [ Max length - 30] */
        private const val QUERY_PARAM_TO_SYMBOL = "tsym"
        /** Comma separated cryptocurrency symbols list to convert into
         [ Min length - 1] [ Max length - 100] */
        private const val QUERY_PARAM_TO_SYMBOLS = "tsyms"
        /** Comma separated cryptocurrency symbols list [ Min length - 1] [ Max length - 1000] */
        private const val QUERY_PARAM_FROM_SYMBOLS = "fsyms"

        private const val CONVERTED_IN = "USD"
        private const val DEFAULT_PAGE = 0
        const val PAGE_SIZE = 10
    }
}
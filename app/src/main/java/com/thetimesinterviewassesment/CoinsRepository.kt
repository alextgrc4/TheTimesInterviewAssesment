package com.thetimesinterviewassesment

import android.content.ContentValues.TAG
import android.util.Log
import com.fasterxml.jackson.module.kotlin.readValue
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.thetimesinterviewassesment.model.Coin
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import java.io.EOFException

class CoinsRepository {

    companion object {
        const val GET_COINS_URL = "https://api.coinpaprika.com/v1/coins"
        const val GET_COIN_BY_ID_URL = "https://api.coinpaprika.com/v1/coins/{ID}"
    }

    private val objectMapper = jacksonObjectMapper()

    suspend fun getCoins(): List<Coin>? = withContext(Dispatchers.IO) {
        val okHttpClient = OkHttpClient()
        parseGetCoinsResponse(okHttpClient.newCall(createRequest(GET_COINS_URL)).execute())
    }

    private fun parseGetCoinsResponse(response: Response): List<Coin>? {
        return try {
            val jsonBody = response.body.string()
            objectMapper.readValue(jsonBody)
        } catch (e: EOFException) {
            Log.e(TAG, "parseGetCoinsResponse: ", e)
            null
        }
    }

    suspend fun getCoinById(id: String): Coin? = withContext(Dispatchers.IO) {
        val okHttpClient = OkHttpClient()
        parseGetCoinByIdResponse(okHttpClient.newCall(createRequest(GET_COIN_BY_ID_URL, id)).execute())
            .takeUnless { id.isEmpty() }
    }

    private fun parseGetCoinByIdResponse(response: Response): Coin? {
        return try {
            val jsonBody = response.body.string()
            objectMapper.readValue(jsonBody)
        } catch (e: EOFException) {
            Log.e(TAG, "parseGetCoinsResponse: ", e)
            null
        }
    }

    private fun createRequest(url: String, substitution: String? = null): Request {
        return Request.Builder()
            .url(
                if (substitution.isNullOrEmpty()) {
                    url
                } else {
                    url.replace("{ID}", substitution)
                }
            )
            .build()
    }
}

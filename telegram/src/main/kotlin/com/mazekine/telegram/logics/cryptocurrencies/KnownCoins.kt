package com.mazekine.telegram.logics.cryptocurrencies

import com.mazekine.telegram.types.Cryptocurrency
import com.mazekine.client.infrastructure.ApiClient
import com.mazekine.client.infrastructure.ByteArrayAdapter
import com.mazekine.client.infrastructure.ClientException
import com.mazekine.client.infrastructure.ClientError
import com.mazekine.client.infrastructure.LocalDateAdapter
import com.mazekine.client.infrastructure.LocalDateTimeAdapter
import com.mazekine.client.infrastructure.ServerException
import com.mazekine.client.infrastructure.ServerError
import com.mazekine.client.infrastructure.MultiValueMap
import com.mazekine.client.infrastructure.RequestConfig
import com.mazekine.client.infrastructure.RequestMethod
import com.mazekine.client.infrastructure.RequestMethod.GET
import com.mazekine.client.infrastructure.ResponseType
import com.mazekine.client.infrastructure.Serializer
import com.mazekine.client.infrastructure.Success
import com.mazekine.client.infrastructure.UUIDAdapter
import com.mazekine.client.infrastructure.toMultiValue
import com.mazekine.telegram.logics.cryptocurrencies.models.CMCResponse
import com.squareup.moshi.Moshi
import com.squareup.moshi.adapters.Rfc3339DateJsonAdapter
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import java.io.File
import java.io.IOException
import java.util.Date

@Suppress("UNCHECKED_CAST")
object KnownCoins {
    /**
     * Internal collection of metadata on coins, loaded from CoinMarketCap file
     */
    private var coins: MutableMap<String, Cryptocurrency> = mutableMapOf()

    /**
     * Moshi instance to parse JSON file
     */
    private val moshi: Moshi = Moshi.Builder()
        .add(
            Date::class.java, Rfc3339DateJsonAdapter()
                .lenient()
                .nullSafe()
        )
        .add(LocalDateTimeAdapter())
        .add(LocalDateAdapter())
        .add(UUIDAdapter())
        .add(ByteArrayAdapter())
        .add(KotlinJsonAdapterFactory())
        .build()

    /**
     * Identifies if the collection of coins was successfully initialized
     */
    var initialized: Boolean = false
        private set

    init {
        this.reload()
    }

    /**
     * <tt>reload()</tt> function is called upon the first request to the object.
     * It builds the collection of data on the coins from CoinMarketCap's feed.
     *
     * It can also be called from external code if the primary initialization failed.
     *
     */
    fun reload() {
        var coinsCollection: MutableMap<String, Cryptocurrency> = mutableMapOf()

        println("[KNOWNCOINS] Loading configuration file...")

        val json: CMCResponse?

        try {
            json = this.moshi.adapter(CMCResponse::class.java).lenient().fromJson(
                File("telegram/src/main/kotlin/com/mazekine/telegram/logics/resources/CMC.json").readText()
            )
        }catch(e: Exception){
            println("[KNOWNCOINS] Something went wrong with loading configuration file. Details:\n${e.message}")
            if(this.initialized){
                println("[KNOWNCOINS] Restored to previous state")
            }
            return
        }

        json!!.let{json ->
            println("[KNOWNCOINS] Successfully loaded coins data. Building collection...")

            json.data!!.forEach{coin ->
                coinsCollection[coin.symbol.toUpperCase()] = Cryptocurrency(
                    coin.symbol.toUpperCase(),
                    coin.name,
                    coin.platform)
            }

            if(coinsCollection.count() > 0) {
                this.coins = coinsCollection
                println("[KNOWNCOINS] Coins collection successfully " +
                        if(this.initialized) "reloaded" else "initialized")
                this.initialized = true
            } else {
                println("[KNOWNCOINS] Something went wrong with coins collection initialization")
                if(this.initialized) {
                    println("[KNOWNCOINS] Restored to previous state")
                }
            }
        }
    }

    /**
     * Checks if the coin with such ticker exists in the list of known coins
     *
     * @param [ticker] A symbolic name of a cryptocurrency, e.g. BTC for Bitcoin. Case-insensitive
     * @return <tt>true</tt> if the coin with such ticker exists in the library, <tt>false</tt> otherwise or if the collection was not duly initialized
     */
    fun contains(ticker: String): Boolean {
        // Check if object was initialized. If not, attempt to do it
        if(!this.initialized){
            this.reload()

            //  If there is still a failure
            if(!this.initialized){
                return false
            }
        }

        return this.coins.contains(ticker.toUpperCase().trim())

/*
        this.coins[ticker.toUpperCase().trim()]!!.let{
            return true
        }

        return false
*/
    }

    /**
     * Let's get details on a cryptocurrency by its ticker
     *
     * @param [ticker] A symbolic name of a cryptocurrency, e.g. BTC for Bitcoin. Case-insensitive
     * @return <tt>Cryptocurrency</tt> object containing metadata on the coin, if exists in the collection, or <tt>null</tt> otherwise or if the collection was not duly initialized.
     * @see [Cryptocurrency]
     */
    operator fun get(ticker: String): Cryptocurrency? {
        // Check if object was initialized. If not, attempt to do it
        if(!this.initialized){
            this.reload()

            //  If there is still a failure
            if(!this.initialized){
                return null
            }
        }

        return coins[ticker.toUpperCase().trim()]
    }
}
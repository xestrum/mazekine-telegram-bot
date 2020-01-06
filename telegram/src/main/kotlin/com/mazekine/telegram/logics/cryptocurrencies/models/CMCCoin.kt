package com.mazekine.telegram.logics.cryptocurrencies.models

data class CMCCoin(val symbol: String = "",
                   val is_active: Int = 0,
                   val last_historical_data: String? = null,
                   val name: String = "",
                   val rank: Int = 0,
                   val id: Int = 0,
                   val slug: String = "",
                   val platform: CMCCoinPlatform? = null,
                   val first_historical_data: String? = null)
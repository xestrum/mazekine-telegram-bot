package com.mazekine.telegram.types

import com.mazekine.telegram.logics.cryptocurrencies.models.CMCCoinPlatform
import java.io.Serializable

data class Cryptocurrency (
    val ticker: String = "",
    val name: String = "",
    val platform: CMCCoinPlatform? = null
) : Serializable
package com.mazekine.telegram.logics.cryptocurrencies.models

data class CMCResponse(val data: List<CMCCoin>?,
                       val status: CMCApiStatus)
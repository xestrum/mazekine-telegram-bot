package com.mazekine.telegram.logics.cryptocurrencies.models

data class CMCApiStatus(val error_message: String? = null,
                        val elapsed: Int = 0,
                        val credit_count: Int = 0,
                        val error_code: Int = 0,
                        val timestamp: String = "",
                        val notice: String? = null)
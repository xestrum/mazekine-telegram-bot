package com.mazekine.telegram.errors

import com.mazekine.telegram.types.DispatchableObject

interface TelegramError : DispatchableObject {
    enum class Error {
        RETRIEVE_UPDATES
    }

    fun getType(): Error
    fun getErrorMessage(): String
}

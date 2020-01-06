package com.mazekine.telegram.configuration

import com.natpryce.konfig.*

object TelegramConfig : PropertyGroup() {
    val token by stringType
    val timeout by intType
}
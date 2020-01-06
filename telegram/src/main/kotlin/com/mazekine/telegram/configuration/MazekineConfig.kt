package com.mazekine.telegram.configuration

import com.natpryce.konfig.*

object MazekineConfig : PropertyGroup() {
    val api_secret by stringType
    val api_address by uriType
}
package com.mazekine.client.infrastructure

/*
import com.squareup.moshi.KotlinJsonAdapterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.Rfc3339DateJsonAdapter
import java.util.*

object Serializer {
    @JvmStatic
    val moshi: Moshi = Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .add(Date::class.java, Rfc3339DateJsonAdapter().nullSafe())
            .build()
}
*/

import com.squareup.moshi.Moshi
import com.squareup.moshi.adapters.Rfc3339DateJsonAdapter
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.mazekine.client.infrastructure.ByteArrayAdapter
import com.mazekine.client.infrastructure.LocalDateAdapter
import com.mazekine.client.infrastructure.LocalDateTimeAdapter
import com.mazekine.client.infrastructure.UUIDAdapter
import java.util.Date

object Serializer {
    @JvmStatic
    val moshi: Moshi = Moshi.Builder()
        .add(Date::class.java, Rfc3339DateJsonAdapter()
            .lenient()
            .nullSafe()
        )
        .add(LocalDateTimeAdapter())
        .add(LocalDateAdapter())
        .add(UUIDAdapter())
        .add(ByteArrayAdapter())
        .add(KotlinJsonAdapterFactory())
        .build()
}

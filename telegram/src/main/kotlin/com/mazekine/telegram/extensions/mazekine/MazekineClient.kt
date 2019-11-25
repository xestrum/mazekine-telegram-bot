package com.mazekine.telegram.extensions.mazekine

import com.mazekine.client.models.*
import com.mazekine.client.apis.*
import com.mazekine.client.infrastructure.*
import java.lang.Exception

class MazekineClient {
    private var authToken: kotlin.String? = null
    private val MAZEKINE_API_PATH: kotlin.String
    private val ADDRESS_API: AddressesApi
    private val PROFILE_API: ProfileApi

    constructor(apiAddress: kotlin.String?, secret: kotlin.String?) {
        MAZEKINE_API_PATH = apiAddress ?: "https://api.mazekine.com/v1"
        PROFILE_API = ProfileApi(MAZEKINE_API_PATH)
        ADDRESS_API = AddressesApi(MAZEKINE_API_PATH)

        try{
            authToken = PROFILE_API.auth(secret ?: "").token
        } catch (e: Exception) {
            println(e.message)
        }
    }

    fun getAuthToken(): String {
        return authToken ?: "Sorry, this user is not authenticated"
    }

    fun getAddressData(
        address: kotlin.String,
        currency: kotlin.String = "BTC",
        wolfhunt: kotlin.Boolean = false,
        forceUpdate: kotlin.Boolean = false
    ) : AddressModel? {

        try {
            val objAddressData: AddressModel
            objAddressData = ADDRESS_API?.getAddress(address, currency)
        } catch(e: ClientException) {

        } catch(e: ServerException) {

        }

    }
}
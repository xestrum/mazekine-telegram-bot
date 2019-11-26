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

        var result: String
        val objAddressData: AddressModel

        try {
            objAddressData = ADDRESS_API?.getAddress(address, currency, wolfhunt, forceUpdate)
            /*with(objAddressData){
                result = "_Address owner:_ " + owner?.firstName + " " + owner?.lastName + "\n"
                result += "_Wallet name:_ " + wallet?.name
                result += "_Transaction ID:_ " + transaction?.id
                result += "_Transactions available_" + transaction?.requestsAvailable
            }*/
        } catch(e: ClientException) {
            println(e.message)
            return null
        } catch(e: ServerException) {
            println(e.message)
            return null
        }

        return objAddressData
    }
}
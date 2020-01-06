/**
* Mazekine.Api
* No description provided (generated by Swagger Codegen https://github.com/swagger-api/swagger-codegen)
*
* OpenAPI spec version: v1
* 
*
* NOTE: This class is auto generated by the swagger code generator program.
* https://github.com/swagger-api/swagger-codegen.git
* Do not edit the class manually.
*/
package com.mazekine.client.apis

import com.mazekine.client.models.AddressModel
import com.mazekine.client.models.ProblemDetails

import com.mazekine.client.infrastructure.ApiClient
import com.mazekine.client.infrastructure.ClientException
import com.mazekine.client.infrastructure.ClientError
import com.mazekine.client.infrastructure.ServerException
import com.mazekine.client.infrastructure.ServerError
import com.mazekine.client.infrastructure.MultiValueMap
import com.mazekine.client.infrastructure.RequestConfig
import com.mazekine.client.infrastructure.RequestMethod
import com.mazekine.client.infrastructure.ResponseType
import com.mazekine.client.infrastructure.Success
import com.mazekine.client.infrastructure.toMultiValue

class AddressesApi(basePath: kotlin.String = "http://localhost") : ApiClient(basePath) {

    /**
     * Find contact by address.
     *
     * @param address
     * @param currency
     * @param wolfhunt  (optional, default to false)
     * @param forceUpdate  (optional, default to false)
     * @return AddressModel
     * @throws UnsupportedOperationException If the API returns an informational or redirection response
     * @throws ClientException If the API returns a client error response
     * @throws ServerException If the API returns a server error response
     */
    @Suppress("UNCHECKED_CAST")
    @Throws(UnsupportedOperationException::class, ClientException::class, ServerException::class)
    fun getAddress(
        address: kotlin.String,
        currency: kotlin.String,
        wolfhunt: kotlin.Boolean?,
        forceUpdate: kotlin.Boolean?,
        authToken: kotlin.String?) : AddressModel {
        val localVariableBody: kotlin.Any? = null
        val localVariableQuery: MultiValueMap = mutableMapOf<kotlin.String, List<kotlin.String>>()
            .apply {
                if (wolfhunt != null) {
                    put("wolfhunt", listOf(wolfhunt.toString()))
                }
                if (forceUpdate != null) {
                    put("forceUpdate", listOf(forceUpdate.toString()))
                }
            }
        val localVariableHeaders: MutableMap<String, String> = mutableMapOf("Authorization" to "Bearer $authToken")
        val localVariableConfig = RequestConfig(
            RequestMethod.GET,
            "/api/addresses/{currency}/{address}".replace("{"+"address"+"}", "$address").replace("{"+"currency"+"}", "$currency"),
            query = localVariableQuery,
            headers = localVariableHeaders
        )
        val localVarResponse = request<AddressModel>(
            localVariableConfig,
            localVariableBody
        )

        return when (localVarResponse.responseType) {
            ResponseType.Success -> (localVarResponse as Success<*>).data as AddressModel
            ResponseType.Informational -> throw UnsupportedOperationException("Client does not support Informational responses.")
            ResponseType.Redirection -> throw UnsupportedOperationException("Client does not support Redirection responses.")
            ResponseType.ClientError -> throw ClientException((localVarResponse as ClientError<*>).body as? String ?: "Client error")
            ResponseType.ServerError -> throw ServerException((localVarResponse as ServerError<*>).message ?: "Server error")
        }
    }

    /**
     * Get image for provider.
     *
     * @param wallet  (optional)
     * @return void
     * @throws UnsupportedOperationException If the API returns an informational or redirection response
     * @throws ClientException If the API returns a client error response
     * @throws ServerException If the API returns a server error response
     */
    @Throws(UnsupportedOperationException::class, ClientException::class, ServerException::class)
    fun getImage(wallet: kotlin.String?) : Unit {
        val localVariableBody: kotlin.Any? = null
        val localVariableQuery: MultiValueMap = mutableMapOf<kotlin.String, List<kotlin.String>>()
            .apply {
                if (wallet != null) {
                    put("wallet", listOf(wallet.toString()))
                }
            }
        val localVariableHeaders: MutableMap<String, String> = mutableMapOf()
        val localVariableConfig = RequestConfig(
            RequestMethod.GET,
            "/api/addresses/providers/image",
            query = localVariableQuery,
            headers = localVariableHeaders
        )
        val localVarResponse = request<Any?>(
            localVariableConfig,
            localVariableBody
        )

        return when (localVarResponse.responseType) {
            ResponseType.Success -> Unit
            ResponseType.Informational -> throw UnsupportedOperationException("Client does not support Informational responses.")
            ResponseType.Redirection -> throw UnsupportedOperationException("Client does not support Redirection responses.")
            ResponseType.ClientError -> throw ClientException((localVarResponse as ClientError<*>).body as? String ?: "Client error")
            ResponseType.ServerError -> throw ServerException((localVarResponse as ServerError<*>).message ?: "Server error")
        }
    }

}
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

import com.mazekine.client.models.DisputeModel
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

class DisputesApi(basePath: kotlin.String = "http://localhost") : ApiClient(basePath) {

    /**
     * Create dispute.
     *
     * @param transactionId
     * @return DisputeModel
     * @throws UnsupportedOperationException If the API returns an informational or redirection response
     * @throws ClientException If the API returns a client error response
     * @throws ServerException If the API returns a server error response
     */
    @Suppress("UNCHECKED_CAST")
    @Throws(UnsupportedOperationException::class, ClientException::class, ServerException::class)
    fun createDispute(transactionId: java.util.UUID) : DisputeModel {
        val localVariableBody: kotlin.Any? = null
        val localVariableQuery: MultiValueMap = mutableMapOf()
        val localVariableHeaders: MutableMap<String, String> = mutableMapOf()
        val localVariableConfig = RequestConfig(
            RequestMethod.POST,
            "/api/disputes/transactions/{transactionId}/disputes".replace("{"+"transactionId"+"}", "$transactionId"),
            query = localVariableQuery,
            headers = localVariableHeaders
        )
        val localVarResponse = request<DisputeModel>(
            localVariableConfig,
            localVariableBody
        )

        return when (localVarResponse.responseType) {
            ResponseType.Success -> (localVarResponse as Success<*>).data as DisputeModel
            ResponseType.Informational -> throw UnsupportedOperationException("Client does not support Informational responses.")
            ResponseType.Redirection -> throw UnsupportedOperationException("Client does not support Redirection responses.")
            ResponseType.ClientError -> throw ClientException((localVarResponse as ClientError<*>).body as? String ?: "Client error")
            ResponseType.ServerError -> throw ServerException((localVarResponse as ServerError<*>).message ?: "Server error")
        }
    }

    /**
     * Get dispute.
     *
     * @param id
     * @return DisputeModel
     * @throws UnsupportedOperationException If the API returns an informational or redirection response
     * @throws ClientException If the API returns a client error response
     * @throws ServerException If the API returns a server error response
     */
    @Suppress("UNCHECKED_CAST")
    @Throws(UnsupportedOperationException::class, ClientException::class, ServerException::class)
    fun get(id: java.util.UUID) : DisputeModel {
        val localVariableBody: kotlin.Any? = null
        val localVariableQuery: MultiValueMap = mutableMapOf()
        val localVariableHeaders: MutableMap<String, String> = mutableMapOf()
        val localVariableConfig = RequestConfig(
            RequestMethod.GET,
            "/api/disputes/{id}".replace("{"+"id"+"}", "$id"),
            query = localVariableQuery,
            headers = localVariableHeaders
        )
        val localVarResponse = request<DisputeModel>(
            localVariableConfig,
            localVariableBody
        )

        return when (localVarResponse.responseType) {
            ResponseType.Success -> (localVarResponse as Success<*>).data as DisputeModel
            ResponseType.Informational -> throw UnsupportedOperationException("Client does not support Informational responses.")
            ResponseType.Redirection -> throw UnsupportedOperationException("Client does not support Redirection responses.")
            ResponseType.ClientError -> throw ClientException((localVarResponse as ClientError<*>).body as? String ?: "Client error")
            ResponseType.ServerError -> throw ServerException((localVarResponse as ServerError<*>).message ?: "Server error")
        }
    }

}
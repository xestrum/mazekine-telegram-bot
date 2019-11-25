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

import com.mazekine.client.infrastructure.*

class DisputesApi(basePath: kotlin.String = "https://api.mazekine.com") : ApiClient(basePath) {

    /**
    * Create dispute.
    * 
    * @param transactionId  
    * @return DisputeModel
    */
    @Suppress("UNCHECKED_CAST")
    fun createDispute(transactionId: java.util.UUID) : DisputeModel {
        val localVariableBody: kotlin.Any? = null
        val localVariableQuery: MultiValueMap = mapOf()
        
        val contentHeaders: kotlin.collections.Map<kotlin.String,kotlin.String> = mapOf()
        val acceptsHeaders: kotlin.collections.Map<kotlin.String,kotlin.String> = mapOf("Accept" to "application/json")
        val localVariableHeaders: kotlin.collections.MutableMap<kotlin.String,kotlin.String> = mutableMapOf()
        localVariableHeaders.putAll(contentHeaders)
        localVariableHeaders.putAll(acceptsHeaders)
        
        val localVariableConfig = RequestConfig(
            RequestMethod.POST,
            "/api/disputes/transactions/{transactionId}/disputes".replace("{"+"transactionId"+"}", "$transactionId"),
            query = localVariableQuery,
            headers = localVariableHeaders
        )
        val response = request<DisputeModel>(
            localVariableConfig,
            localVariableBody
        )

        return when (response.responseType) {
            ResponseType.Success -> (response as Success<*>).data as DisputeModel
            ResponseType.Informational -> TODO()
            ResponseType.Redirection -> TODO()
            ResponseType.ClientError -> throw ClientException((response as ClientError<*>).body as? String ?: "Client error")
            ResponseType.ServerError -> throw ServerException((response as ServerError<*>).message ?: "Server error")
            else -> throw kotlin.IllegalStateException("Undefined ResponseType.")
        }
    }

    /**
    * Get dispute.
    * 
    * @param id  
    * @return DisputeModel
    */
    @Suppress("UNCHECKED_CAST")
    fun get(id: java.util.UUID) : DisputeModel {
        val localVariableBody: kotlin.Any? = null
        val localVariableQuery: MultiValueMap = mapOf()
        
        val contentHeaders: kotlin.collections.Map<kotlin.String,kotlin.String> = mapOf()
        val acceptsHeaders: kotlin.collections.Map<kotlin.String,kotlin.String> = mapOf("Accept" to "application/json")
        val localVariableHeaders: kotlin.collections.MutableMap<kotlin.String,kotlin.String> = mutableMapOf()
        localVariableHeaders.putAll(contentHeaders)
        localVariableHeaders.putAll(acceptsHeaders)
        
        val localVariableConfig = RequestConfig(
            RequestMethod.GET,
            "/api/disputes/{id}".replace("{"+"id"+"}", "$id"),
            query = localVariableQuery,
            headers = localVariableHeaders
        )
        val response = request<DisputeModel>(
            localVariableConfig,
            localVariableBody
        )

        return when (response.responseType) {
            ResponseType.Success -> (response as Success<*>).data as DisputeModel
            ResponseType.Informational -> TODO()
            ResponseType.Redirection -> TODO()
            ResponseType.ClientError -> throw ClientException((response as ClientError<*>).body as? String ?: "Client error")
            ResponseType.ServerError -> throw ServerException((response as ServerError<*>).message ?: "Server error")
            else -> throw kotlin.IllegalStateException("Undefined ResponseType.")
        }
    }

}

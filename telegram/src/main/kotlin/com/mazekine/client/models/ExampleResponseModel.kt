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
package com.mazekine.client.models


/**
 * 
 * @param userId The ID of user in internal system.
 * @param country The country of user.
 * @param firstName The first name of user.
 * @param lastName The last name of user.
 * @param addressCreatedDateUtc The date of address creation.
 * @param isVerified Is user verified or is user data its own.
 */
data class ExampleResponseModel (
    /* The ID of user in internal system. */
    val userId: kotlin.String? = null,
    /* The country of user. */
    val country: kotlin.String? = null,
    /* The first name of user. */
    val firstName: kotlin.String? = null,
    /* The last name of user. */
    val lastName: kotlin.String? = null,
    /* The date of address creation. */
    val addressCreatedDateUtc: java.time.LocalDateTime? = null,
    /* Is user verified or is user data its own. */
    val isVerified: kotlin.Boolean? = null
) {

}


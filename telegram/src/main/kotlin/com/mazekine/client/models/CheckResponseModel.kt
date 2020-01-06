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
 * @param isCorrect Is setup correctly.
 * @param errors The errors during checking.
 */
data class CheckResponseModel (
    /* Is setup correctly. */
    val isCorrect: kotlin.Boolean? = null,
    /* The errors during checking. */
    val errors: kotlin.Array<kotlin.String>? = null
) {

}

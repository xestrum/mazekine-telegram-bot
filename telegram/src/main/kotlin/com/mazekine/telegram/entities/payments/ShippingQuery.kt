package com.mazekine.telegram.entities.payments

import com.google.gson.annotations.SerializedName
import com.mazekine.telegram.entities.User

data class ShippingQuery(
    val id: String,
    val from: User,
    @SerializedName("invoice_payload") val invoicePayload: String,
    @SerializedName("shipping_address") val shippingAddress: ShippingAddress
)

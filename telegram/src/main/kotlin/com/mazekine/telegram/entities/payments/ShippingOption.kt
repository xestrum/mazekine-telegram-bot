package com.mazekine.telegram.entities.payments

data class ShippingOption(
    val id: String,
    val title: String,
    val prices: List<LabeledPrice>
)

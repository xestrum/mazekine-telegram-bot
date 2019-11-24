package com.mazekine.telegram.entities.payments

import java.math.BigInteger

data class LabeledPrice(
    val label: String? = null,
    val amount: BigInteger
)

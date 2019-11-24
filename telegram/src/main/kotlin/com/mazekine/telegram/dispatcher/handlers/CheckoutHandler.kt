package com.mazekine.telegram.dispatcher.handlers

import com.mazekine.telegram.HandleUpdate
import com.mazekine.telegram.entities.Update

class CheckoutHandler(handleUpdate: HandleUpdate) : Handler(handleUpdate) {
    override val groupIdentifier: String
        get() = "payment"

    override fun checkUpdate(update: Update): Boolean {
        return update.preCheckoutQuery != null
    }
}

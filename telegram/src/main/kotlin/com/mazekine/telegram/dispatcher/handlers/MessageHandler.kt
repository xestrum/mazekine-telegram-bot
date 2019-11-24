package com.mazekine.telegram.dispatcher.handlers

import com.mazekine.telegram.HandleUpdate
import com.mazekine.telegram.entities.Update
import com.mazekine.telegram.extensions.filters.Filter

class MessageHandler(
    handlerCallback: HandleUpdate,
    private val filter: Filter
) : Handler(handlerCallback) {

    override val groupIdentifier: String
        get() = "MessageHandler"

    override fun checkUpdate(update: Update): Boolean =
        if (update.message == null) {
            false
        } else {
            filter.checkFor(update.message)
        }
}

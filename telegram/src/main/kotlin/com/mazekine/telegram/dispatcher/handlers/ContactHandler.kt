package com.mazekine.telegram.dispatcher.handlers

import com.mazekine.telegram.Bot
import com.mazekine.telegram.ContactHandleUpdate
import com.mazekine.telegram.HandleUpdate
import com.mazekine.telegram.entities.Update

class ContactHandler(handleUpdate: ContactHandleUpdate) : Handler(ContactHandleUpdateProxy(handleUpdate)) {

    override val groupIdentifier: String
        get() = "System"

    override fun checkUpdate(update: Update): Boolean {
        return update.message?.contact != null
    }
}

private class ContactHandleUpdateProxy(private val handleUpdate: ContactHandleUpdate) : HandleUpdate {
    override fun invoke(bot: Bot, update: Update) {
        handleUpdate(bot, update, update.message?.contact!!)
    }
}

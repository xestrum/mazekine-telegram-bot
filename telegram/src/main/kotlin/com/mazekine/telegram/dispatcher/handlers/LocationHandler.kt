package com.mazekine.telegram.dispatcher.handlers

import com.mazekine.telegram.Bot
import com.mazekine.telegram.HandleUpdate
import com.mazekine.telegram.LocationHandleUpdate
import com.mazekine.telegram.entities.Update

class LocationHandler(handleUpdate: LocationHandleUpdate) : Handler(LocationHandleUpdateProxy(handleUpdate)) {
    override val groupIdentifier: String
        get() = "System"

    override fun checkUpdate(update: Update): Boolean {
        return update.message?.location != null
    }
}

private class LocationHandleUpdateProxy(private val handleUpdate: LocationHandleUpdate) : HandleUpdate {
    override fun invoke(bot: Bot, update: Update) {
        handleUpdate(bot, update, update.message?.location!!)
    }
}

package com.mazekine.telegram.dispatcher.handlers

import com.mazekine.telegram.HandleUpdate
import com.mazekine.telegram.entities.Update

class ChannelHandler(handleUpdate: HandleUpdate) : Handler(handleUpdate) {
    override val groupIdentifier: String
        get() = "channel"

    override fun checkUpdate(update: Update): Boolean {
        return update.channelPost != null || update.editedChannelPost != null
    }
}

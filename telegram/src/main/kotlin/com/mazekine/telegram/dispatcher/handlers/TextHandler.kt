package com.mazekine.telegram.dispatcher.handlers

import com.mazekine.telegram.HandleUpdate
import com.mazekine.telegram.entities.Update

class TextHandler(private val text: String? = null, handler: HandleUpdate) : Handler(handler) {
    override val groupIdentifier: String = "CommandHandler"

    override fun checkUpdate(update: Update): Boolean {
        if (update.message?.text != null && text == null) return true
        else if (text != null) {
            return (update.message?.text != null && update.message.text.toLowerCase().contains(text.toLowerCase()))
        }
        return false
    }
}

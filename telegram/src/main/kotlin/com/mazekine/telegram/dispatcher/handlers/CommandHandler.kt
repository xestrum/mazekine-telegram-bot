package com.mazekine.telegram.dispatcher.handlers

import com.mazekine.telegram.Bot
import com.mazekine.telegram.CommandHandleUpdate
import com.mazekine.telegram.HandleUpdate
import com.mazekine.telegram.entities.Update

class CommandHandler(private val command: String, handler: HandleUpdate) : Handler(handler) {

    constructor(command: String, handler: CommandHandleUpdate) : this(command, CommandHandleUpdateProxy(handler))

    override val groupIdentifier: String = "CommandHandler"

    override fun checkUpdate(update: Update): Boolean {
        return (update.message?.text != null && update.message.text.startsWith("/") &&
            update.message.text.drop(1).split(" ")[0].split("@")[0] == command)
    }
}

private class CommandHandleUpdateProxy(private val handleUpdate: CommandHandleUpdate) : HandleUpdate {
    override fun invoke(bot: Bot, update: Update) {
        handleUpdate(bot, update, update.message?.text?.split("\\s+".toRegex())?.drop(1) ?: listOf())
    }
}
package com.mazekine.telegram.dispatcher.handlers.media

import com.mazekine.telegram.Bot
import com.mazekine.telegram.HandleUpdate
import com.mazekine.telegram.dispatcher.handlers.Handler
import com.mazekine.telegram.entities.Update

abstract class MediaHandler<T>(
    handleMediaUpdate: (Bot, Update, T) -> Unit,
    toMedia: (Update) -> T,
    private val predicate: (Update) -> Boolean
) : Handler(MediaHandlerProxy(handleMediaUpdate, toMedia)) {

    override val groupIdentifier: String
        get() = "MediaHandler"

    override fun checkUpdate(update: Update): Boolean = predicate(update)
}

private class MediaHandlerProxy<T>(
    private val handleMediaUpdate: (Bot, Update, T) -> Unit,
    private val toMedia: Update.() -> T
) : HandleUpdate {

    override fun invoke(bot: Bot, update: Update) {
        handleMediaUpdate(bot, update, update.toMedia())
    }
}

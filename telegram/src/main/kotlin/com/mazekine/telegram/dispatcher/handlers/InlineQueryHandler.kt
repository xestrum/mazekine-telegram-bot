package com.mazekine.telegram.dispatcher.handlers

import com.mazekine.telegram.Bot
import com.mazekine.telegram.HandleInlineQuery
import com.mazekine.telegram.HandleUpdate
import com.mazekine.telegram.entities.Update

class InlineQueryHandler(
    handleInlineQuery: HandleInlineQuery
) : Handler(InlineQueryHandlerProxy(handleInlineQuery)) {
    override val groupIdentifier: String
        get() = "InlineQueryHandler"

    override fun checkUpdate(update: Update): Boolean = update.inlineQuery != null
}

private class InlineQueryHandlerProxy(
    private val handleInlineQuery: HandleInlineQuery
) : HandleUpdate {

    override fun invoke(bot: Bot, update: Update) {
        val inlineQuery = update.inlineQuery
        checkNotNull(inlineQuery)
        handleInlineQuery(bot, inlineQuery)
    }
}

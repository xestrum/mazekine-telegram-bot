package com.mazekine.telegram.logics.entities

import com.mazekine.telegram.logics.entities.BotContext

class BotContextRow private constructor(
    val parent: BotContext,
    var children: MutableList<BotContext>
) : BotContext(
    parent = parent
)
{
}
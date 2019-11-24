package com.mazekine.telegram.dispatcher.handlers.media

import com.mazekine.telegram.HandleGameUpdate
import com.mazekine.telegram.entities.Game
import com.mazekine.telegram.entities.Update

class GameHandler(
    handleGameUpdate: HandleGameUpdate
) : MediaHandler<Game>(
    handleGameUpdate,
    GameHandlerFunctions::toMedia,
    GameHandlerFunctions::predicate
)

private object GameHandlerFunctions {

    fun toMedia(update: Update): Game {
        val game = update.message?.game
        checkNotNull(game)
        return game
    }

    fun predicate(update: Update): Boolean = update.message?.game != null
}

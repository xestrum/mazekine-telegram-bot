package com.mazekine.telegram.dispatcher.handlers.media

import com.mazekine.telegram.HandleVoiceUpdate
import com.mazekine.telegram.entities.Update
import com.mazekine.telegram.entities.Voice

class VoiceHandler(
    handleVoiceUpdate: HandleVoiceUpdate
) : MediaHandler<Voice>(
    handleVoiceUpdate,
    VoiceHandlerFunctions::toMedia,
    VoiceHandlerFunctions::predicate
)

private object VoiceHandlerFunctions {

    fun toMedia(update: Update): Voice {
        val voice = update.message?.voice
        checkNotNull(voice)
        return voice
    }

    fun predicate(update: Update): Boolean = update.message?.voice != null
}

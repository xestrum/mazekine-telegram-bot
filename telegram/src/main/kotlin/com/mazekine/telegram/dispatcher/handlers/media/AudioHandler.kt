package com.mazekine.telegram.dispatcher.handlers.media

import com.mazekine.telegram.HandleAudioUpdate
import com.mazekine.telegram.entities.Audio
import com.mazekine.telegram.entities.Update

class AudioHandler(
    handleAudioUpdate: HandleAudioUpdate
) : MediaHandler<Audio>(
    handleAudioUpdate,
    AudioHandlerFunctions::toMedia,
    AudioHandlerFunctions::predicate
)

private object AudioHandlerFunctions {

    fun toMedia(update: Update): Audio {
        val audio = update.message?.audio
        checkNotNull(audio)
        return audio
    }

    fun predicate(update: Update): Boolean = update.message?.audio != null
}

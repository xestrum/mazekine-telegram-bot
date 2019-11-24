package com.mazekine.telegram.dispatcher.handlers.media

import com.mazekine.telegram.HandleVideoNoteUpdate
import com.mazekine.telegram.entities.Update
import com.mazekine.telegram.entities.VideoNote

class VideoNoteHandler(
    handleVideoNoteUpdate: HandleVideoNoteUpdate
) : MediaHandler<VideoNote>(
    handleVideoNoteUpdate,
    VideoNoteHandlerFunctions::toMedia,
    VideoNoteHandlerFunctions::predicate
)

private object VideoNoteHandlerFunctions {

    fun toMedia(update: Update): VideoNote {
        val videoNote = update.message?.videoNote
        checkNotNull(videoNote)
        return videoNote
    }

    fun predicate(update: Update): Boolean = update.message?.videoNote != null
}

package com.mazekine.telegram.dispatcher.handlers.media

import com.mazekine.telegram.HandleVideoUpdate
import com.mazekine.telegram.entities.Update
import com.mazekine.telegram.entities.Video

class VideoHandler(
    handleVideoUpdate: HandleVideoUpdate
) : MediaHandler<Video>(
    handleVideoUpdate,
    VideoHandlerFunctions::toMedia,
    VideoHandlerFunctions::predicate
)

private object VideoHandlerFunctions {

    fun toMedia(update: Update): Video {
        val video = update.message?.video
        checkNotNull(video)
        return video
    }

    fun predicate(update: Update): Boolean = update.message?.video != null
}

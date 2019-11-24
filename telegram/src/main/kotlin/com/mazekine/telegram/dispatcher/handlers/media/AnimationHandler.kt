package com.mazekine.telegram.dispatcher.handlers.media

import com.mazekine.telegram.HandleAnimationUpdate
import com.mazekine.telegram.entities.Animation
import com.mazekine.telegram.entities.Update

class AnimationHandler(
    handleAnimationUpdate: HandleAnimationUpdate
) : MediaHandler<Animation>(
    handleAnimationUpdate,
    AnimationHandlerFunctions::toMedia,
    AnimationHandlerFunctions::predicate
)

private object AnimationHandlerFunctions {

    fun toMedia(update: Update): Animation {
        val animation = update.message?.animation
        checkNotNull(animation)
        return animation
    }

    fun predicate(update: Update): Boolean = update.message?.animation != null
}

package com.mazekine.telegram.dispatcher.handlers.media

import com.mazekine.telegram.HandleStickerUpdate
import com.mazekine.telegram.entities.Update
import com.mazekine.telegram.entities.stickers.Sticker

class StickerHandler(
    handleStickerUpdate: HandleStickerUpdate
) : MediaHandler<Sticker>(
    handleStickerUpdate,
    StickerHandlerFunctions::toMedia,
    StickerHandlerFunctions::predicate
)

private object StickerHandlerFunctions {

    fun toMedia(update: Update): Sticker {
        val sticker = update.message?.sticker
        checkNotNull(sticker)
        return sticker
    }

    fun predicate(update: Update): Boolean = update.message?.sticker != null
}

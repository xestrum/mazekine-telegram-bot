package com.mazekine.telegram.dispatcher.handlers.media

import com.mazekine.telegram.HandlePhotosUpdate
import com.mazekine.telegram.entities.PhotoSize
import com.mazekine.telegram.entities.Update

class PhotosHandler(
    handlePhotosUpdate: HandlePhotosUpdate
) : MediaHandler<List<PhotoSize>>(
    handlePhotosUpdate,
    PhotosHandlerFunctions::toMedia,
    PhotosHandlerFunctions::predicate
)

private object PhotosHandlerFunctions {

    fun toMedia(update: Update): List<PhotoSize> {
        val photos = update.message?.photo
        checkNotNull(photos)
        return photos
    }

    fun predicate(update: Update): Boolean = update.message?.photo != null
}

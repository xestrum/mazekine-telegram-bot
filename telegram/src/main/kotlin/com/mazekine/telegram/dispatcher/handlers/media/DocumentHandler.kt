package com.mazekine.telegram.dispatcher.handlers.media

import com.mazekine.telegram.HandleDocumentUpdate
import com.mazekine.telegram.entities.Document
import com.mazekine.telegram.entities.Update

class DocumentHandler(
    handleDocumentUpdate: HandleDocumentUpdate
) : MediaHandler<Document>(
    handleDocumentUpdate,
    DocumentHandlerFunctions::toMedia,
    DocumentHandlerFunctions::predicate
)

private object DocumentHandlerFunctions {

    fun toMedia(update: Update): Document {
        val document = update.message?.document
        checkNotNull(document)
        return document
    }

    fun predicate(update: Update): Boolean = update.message?.document != null
}

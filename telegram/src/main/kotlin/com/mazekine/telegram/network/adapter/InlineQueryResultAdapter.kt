package com.mazekine.telegram.network.adapter

import com.google.gson.JsonElement
import com.google.gson.JsonSerializationContext
import com.google.gson.JsonSerializer
import java.lang.reflect.Type
import com.mazekine.telegram.entities.inlinequeryresults.InlineQueryResult
import com.mazekine.telegram.entities.inlinequeryresults.InlineQueryResult.Article
import com.mazekine.telegram.entities.inlinequeryresults.InlineQueryResult.Audio
import com.mazekine.telegram.entities.inlinequeryresults.InlineQueryResult.CachedAudio
import com.mazekine.telegram.entities.inlinequeryresults.InlineQueryResult.CachedDocument
import com.mazekine.telegram.entities.inlinequeryresults.InlineQueryResult.CachedGif
import com.mazekine.telegram.entities.inlinequeryresults.InlineQueryResult.CachedMpeg4Gif
import com.mazekine.telegram.entities.inlinequeryresults.InlineQueryResult.CachedSticker
import com.mazekine.telegram.entities.inlinequeryresults.InlineQueryResult.CachedVideo
import com.mazekine.telegram.entities.inlinequeryresults.InlineQueryResult.CachedVoice
import com.mazekine.telegram.entities.inlinequeryresults.InlineQueryResult.Contact
import com.mazekine.telegram.entities.inlinequeryresults.InlineQueryResult.Document
import com.mazekine.telegram.entities.inlinequeryresults.InlineQueryResult.Game
import com.mazekine.telegram.entities.inlinequeryresults.InlineQueryResult.Gif
import com.mazekine.telegram.entities.inlinequeryresults.InlineQueryResult.Location
import com.mazekine.telegram.entities.inlinequeryresults.InlineQueryResult.Mpeg4Gif
import com.mazekine.telegram.entities.inlinequeryresults.InlineQueryResult.Photo
import com.mazekine.telegram.entities.inlinequeryresults.InlineQueryResult.Venue
import com.mazekine.telegram.entities.inlinequeryresults.InlineQueryResult.Video
import com.mazekine.telegram.entities.inlinequeryresults.InlineQueryResult.Voice

class InlineQueryResultAdapter : JsonSerializer<InlineQueryResult> {

    override fun serialize(
        src: InlineQueryResult,
        typeOfSrc: Type,
        context: JsonSerializationContext
    ): JsonElement = when (src) {
        is Article -> context.serialize(src, Article::class.java)
        is Photo -> context.serialize(src, Photo::class.java)
        is Gif -> context.serialize(src, Gif::class.java)
        is Mpeg4Gif -> context.serialize(src, Mpeg4Gif::class.java)
        is Video -> context.serialize(src, Video::class.java)
        is Audio -> context.serialize(src, Audio::class.java)
        is Voice -> context.serialize(src, Voice::class.java)
        is Document -> context.serialize(src, Document::class.java)
        is Location -> context.serialize(src, Location::class.java)
        is Venue -> context.serialize(src, Venue::class.java)
        is Contact -> context.serialize(src, Contact::class.java)
        is Game -> context.serialize(src, Game::class.java)
        is CachedAudio -> context.serialize(src, CachedAudio::class.java)
        is CachedDocument -> context.serialize(src, CachedDocument::class.java)
        is CachedGif -> context.serialize(src, CachedGif::class.java)
        is CachedMpeg4Gif -> context.serialize(src, CachedMpeg4Gif::class.java)
        is CachedSticker -> context.serialize(src, CachedSticker::class.java)
        is CachedVideo -> context.serialize(src, CachedVideo::class.java)
        is CachedVoice -> context.serialize(src, CachedVoice::class.java)
    }
}

package com.mazekine.telegram

import com.mazekine.telegram.entities.Animation
import com.mazekine.telegram.entities.Audio
import com.mazekine.telegram.entities.Contact
import com.mazekine.telegram.entities.Document
import com.mazekine.telegram.entities.Game
import com.mazekine.telegram.entities.InlineQuery
import com.mazekine.telegram.entities.Location
import com.mazekine.telegram.entities.PhotoSize
import com.mazekine.telegram.entities.Update
import com.mazekine.telegram.entities.Video
import com.mazekine.telegram.entities.VideoNote
import com.mazekine.telegram.entities.Voice
import com.mazekine.telegram.entities.stickers.Sticker
import com.mazekine.telegram.errors.TelegramError

typealias HandleUpdate = (Bot, Update) -> Unit

typealias HandleError = (Bot, TelegramError) -> Unit

typealias CommandHandleUpdate = (Bot, Update, List<String>) -> Unit

typealias ContactHandleUpdate = (Bot, Update, Contact) -> Unit

typealias LocationHandleUpdate = (Bot, Update, Location) -> Unit

typealias HandleInlineQuery = (Bot, InlineQuery) -> Unit

typealias HandleAudioUpdate = (Bot, Update, Audio) -> Unit
typealias HandleDocumentUpdate = (Bot, Update, Document) -> Unit
typealias HandleAnimationUpdate = (Bot, Update, Animation) -> Unit
typealias HandleGameUpdate = (Bot, Update, Game) -> Unit
typealias HandlePhotosUpdate = (Bot, Update, List<PhotoSize>) -> Unit
typealias HandleStickerUpdate = (Bot, Update, Sticker) -> Unit
typealias HandleVideoUpdate = (Bot, Update, Video) -> Unit
typealias HandleVoiceUpdate = (Bot, Update, Voice) -> Unit
typealias HandleVideoNoteUpdate = (Bot, Update, VideoNote) -> Unit

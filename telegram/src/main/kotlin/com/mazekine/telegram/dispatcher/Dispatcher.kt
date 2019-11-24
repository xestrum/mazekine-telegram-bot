package com.mazekine.telegram.dispatcher

import java.util.concurrent.BlockingQueue
import java.util.concurrent.LinkedBlockingQueue
import com.mazekine.telegram.Bot
import com.mazekine.telegram.CommandHandleUpdate
import com.mazekine.telegram.ContactHandleUpdate
import com.mazekine.telegram.HandleAnimationUpdate
import com.mazekine.telegram.HandleAudioUpdate
import com.mazekine.telegram.HandleDocumentUpdate
import com.mazekine.telegram.HandleError
import com.mazekine.telegram.HandleGameUpdate
import com.mazekine.telegram.HandleInlineQuery
import com.mazekine.telegram.HandlePhotosUpdate
import com.mazekine.telegram.HandleStickerUpdate
import com.mazekine.telegram.HandleUpdate
import com.mazekine.telegram.HandleVideoNoteUpdate
import com.mazekine.telegram.HandleVideoUpdate
import com.mazekine.telegram.HandleVoiceUpdate
import com.mazekine.telegram.LocationHandleUpdate
import com.mazekine.telegram.dispatcher.handlers.CallbackQueryHandler
import com.mazekine.telegram.dispatcher.handlers.ChannelHandler
import com.mazekine.telegram.dispatcher.handlers.CheckoutHandler
import com.mazekine.telegram.dispatcher.handlers.CommandHandler
import com.mazekine.telegram.dispatcher.handlers.ContactHandler
import com.mazekine.telegram.dispatcher.handlers.Handler
import com.mazekine.telegram.dispatcher.handlers.InlineQueryHandler
import com.mazekine.telegram.dispatcher.handlers.LocationHandler
import com.mazekine.telegram.dispatcher.handlers.MessageHandler
import com.mazekine.telegram.dispatcher.handlers.TextHandler
import com.mazekine.telegram.dispatcher.handlers.media.AnimationHandler
import com.mazekine.telegram.dispatcher.handlers.media.AudioHandler
import com.mazekine.telegram.dispatcher.handlers.media.DocumentHandler
import com.mazekine.telegram.dispatcher.handlers.media.GameHandler
import com.mazekine.telegram.dispatcher.handlers.media.PhotosHandler
import com.mazekine.telegram.dispatcher.handlers.media.StickerHandler
import com.mazekine.telegram.dispatcher.handlers.media.VideoHandler
import com.mazekine.telegram.dispatcher.handlers.media.VideoNoteHandler
import com.mazekine.telegram.dispatcher.handlers.media.VoiceHandler
import com.mazekine.telegram.entities.Update
import com.mazekine.telegram.errors.TelegramError
import com.mazekine.telegram.extensions.filters.Filter
import com.mazekine.telegram.types.DispatchableObject

fun Dispatcher.message(filter: Filter, handleUpdate: HandleUpdate) {
    addHandler(MessageHandler(handleUpdate, filter))
}

fun Dispatcher.command(command: String, body: HandleUpdate) {
    addHandler(CommandHandler(command, body))
}

fun Dispatcher.command(command: String, body: CommandHandleUpdate) {
    addHandler(CommandHandler(command, body))
}

fun Dispatcher.text(text: String? = null, body: HandleUpdate) {
    addHandler(TextHandler(text, body))
}

fun Dispatcher.callbackQuery(data: String? = null, body: HandleUpdate) {
    addHandler(CallbackQueryHandler(callbackData = data, handler = body))
}

fun Dispatcher.callbackQuery(callbackQueryHandler: CallbackQueryHandler) {
    addHandler(callbackQueryHandler)
}

fun Dispatcher.contact(handleUpdate: ContactHandleUpdate) {
    addHandler(ContactHandler(handleUpdate))
}

fun Dispatcher.location(handleUpdate: LocationHandleUpdate) {
    addHandler(LocationHandler(handleUpdate))
}

fun Dispatcher.telegramError(body: HandleError) {
    addErrorHandler(body)
}

fun Dispatcher.preCheckoutQuery(body: HandleUpdate) {
    addHandler(CheckoutHandler(body))
}

fun Dispatcher.channel(body: HandleUpdate) {
    addHandler(ChannelHandler(body))
}

fun Dispatcher.inlineQuery(body: HandleInlineQuery) {
    addHandler(InlineQueryHandler(body))
}

fun Dispatcher.audio(body: HandleAudioUpdate) {
    addHandler(AudioHandler(body))
}

fun Dispatcher.document(body: HandleDocumentUpdate) {
    addHandler(DocumentHandler(body))
}

fun Dispatcher.animation(body: HandleAnimationUpdate) {
    addHandler(AnimationHandler(body))
}

fun Dispatcher.game(body: HandleGameUpdate) {
    addHandler(GameHandler(body))
}

fun Dispatcher.photos(body: HandlePhotosUpdate) {
    addHandler(PhotosHandler(body))
}

fun Dispatcher.sticker(body: HandleStickerUpdate) {
    addHandler(StickerHandler(body))
}

fun Dispatcher.video(body: HandleVideoUpdate) {
    addHandler(VideoHandler(body))
}

fun Dispatcher.voice(body: HandleVoiceUpdate) {
    addHandler(VoiceHandler(body))
}

fun Dispatcher.videoNote(body: HandleVideoNoteUpdate) {
    addHandler(VideoNoteHandler(body))
}

class Dispatcher {

    lateinit var bot: Bot

    val updatesQueue: BlockingQueue<DispatchableObject> = LinkedBlockingQueue<DispatchableObject>()

    private val commandHandlers = mutableMapOf<String, ArrayList<Handler>>()
    private val errorHandlers = arrayListOf<HandleError>()
    private var stopped = false

    fun startCheckingUpdates() {
        stopped = false
        checkQueueUpdates()
    }

    private fun checkQueueUpdates() {
        while (!Thread.currentThread().isInterrupted && !stopped) {
            val item = updatesQueue.take()
            if (item != null) {
                if (item is Update) handleUpdate(item)
                else if (item is TelegramError) handleError(item)
            }
        }
    }

    fun addHandler(handler: Handler) {
        var handlers = commandHandlers[handler.groupIdentifier]

        if (handlers == null) {
            handlers = arrayListOf()
            commandHandlers[handler.groupIdentifier] = handlers
        }

        handlers.add(handler)
    }

    fun removeHandler(handler: Handler) {
        commandHandlers[handler.groupIdentifier]?.remove(handler)
    }

    fun addErrorHandler(errorHandler: HandleError) {
        errorHandlers.add(errorHandler)
    }

    fun removeErrorHandler(errorHandler: HandleError) {
        errorHandlers.remove(errorHandler)
    }

    private fun handleUpdate(update: Update) {
        for (group in commandHandlers) {
            group.value
                .filter { it.checkUpdate(update) }
                .forEach { it.handlerCallback(bot, update) }
        }
    }

    private fun handleError(error: TelegramError) {
        errorHandlers.forEach {
            it(bot, error)
        }
    }

    internal fun stopCheckingUpdates() {
        stopped = true
    }
}

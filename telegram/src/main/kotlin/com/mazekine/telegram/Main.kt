package com.mazekine.telegram

import com.mazekine.client.apis.AddressesApi
import com.mazekine.client.apis.ProfileApi
import com.mazekine.client.models.AuthModel
import com.mazekine.telegram.HandleUpdate
import com.mazekine.telegram.bot
import com.mazekine.telegram.dispatch
import com.mazekine.telegram.dispatcher.callbackQuery
import com.mazekine.telegram.dispatcher.channel
import com.mazekine.telegram.dispatcher.command
import com.mazekine.telegram.dispatcher.contact
import com.mazekine.telegram.dispatcher.handlers.CallbackQueryHandler
import com.mazekine.telegram.dispatcher.inlineQuery
import com.mazekine.telegram.dispatcher.location
import com.mazekine.telegram.dispatcher.message
import com.mazekine.telegram.dispatcher.photos
import com.mazekine.telegram.dispatcher.telegramError
import com.mazekine.telegram.dispatcher.text
import com.mazekine.telegram.entities.InlineKeyboardButton
import com.mazekine.telegram.entities.InlineKeyboardMarkup
import com.mazekine.telegram.entities.KeyboardButton
import com.mazekine.telegram.entities.KeyboardReplyMarkup
import com.mazekine.telegram.entities.ParseMode.MARKDOWN
import com.mazekine.telegram.entities.ReplyKeyboardRemove
import com.mazekine.telegram.entities.inlinequeryresults.InlineQueryResult
import com.mazekine.telegram.entities.inlinequeryresults.InputMessageContent
import com.mazekine.telegram.extensions.filters.Filter
import com.mazekine.telegram.extensions.mazekine.MazekineClient
import com.mazekine.telegram.network.fold
import okhttp3.logging.HttpLoggingInterceptor

fun main(args: Array<String>) {

    val bot = bot {

        token = "1000920535:AAEyvxvwq7dpCySM_MsopmzuNZbOew1rb-E"
        timeout = 30
        logLevel = HttpLoggingInterceptor.Level.BODY

        dispatch {
            message(Filter.Sticker) { bot, update ->
                bot.sendMessage(update.message!!.chat.id, text = "You have received an awesome sticker \\o/")
            }

            message(Filter.Reply or Filter.Forward) { bot, update ->
                bot.sendMessage(update.message!!.chat.id, text = "someone is replying or forwarding messages ...")
            }

            command("check") { bot, update ->

                val strSecret: String = "1ff1f9e1-8d83-4d36-b207-6f8a4d918958"
                val objProfile: ProfileApi = ProfileApi()
                val txt: kotlin.String
                val token: kotlin.String

                /*val mzknResult = objProfile.auth("1ff1f9e1-8d83-4d36-b207-6f8a4d918958")
                if(mzknResult is AuthModel) {
                    token = mzknResult.token!!
                    txt = token
                } else {
                    txt = "Unknown exception"
                }*/

                val objMazekineClient: MazekineClient = MazekineClient(
                    "https://api.mazekine.com",
                    strSecret
                )

                txt = objMazekineClient.getAuthToken()

                val result = bot.sendMessage(chatId = update.message!!.chat.id, text = "I hear you! Result: $txt")

                result.fold({

                }, {

                })
            }

            command("start") { bot, update ->

                val result = bot.sendMessage(chatId = update.message!!.chat.id, text = "Bot started")

                result.fold({
                    // do something here with the response
                }, {
                    // do something with the error
                })
            }

            command("hello") { bot, update ->

                val result = bot.sendMessage(chatId = update.message!!.chat.id, text = "Hello, world!")

                result.fold({
                    // do something here with the response
                }, {
                    // do something with the error
                })
            }

            command("commandWithArgs") { bot, update, args ->
                val joinedArgs = args.joinToString()
                val response = if (!joinedArgs.isNullOrBlank()) joinedArgs else "There is no text apart from command!"
                bot.sendMessage(chatId = update.message!!.chat.id, text = response)
            }

            command("markdown") { bot, update ->
                val markdownText = "_Cool message_: *Markdown* is `beatiful` :P"
                bot.sendMessage(
                    chatId = update.message!!.chat.id,
                    text = markdownText,
                    parseMode = MARKDOWN
                )
            }

            command("inlineButtons") { bot, update ->
                val chatId = update.message?.chat?.id ?: return@command

                val inlineKeyboardMarkup = InlineKeyboardMarkup(generateButtons())
                bot.sendMessage(chatId = chatId, text = "Hello, inline buttons!", replyMarkup = inlineKeyboardMarkup)
            }

            command("userButtons") { bot, update ->
                val chatId = update.message?.chat?.id ?: return@command

                val keyboardMarkup = KeyboardReplyMarkup(keyboard = generateUsersButton(), resizeKeyboard = true)
                bot.sendMessage(chatId = chatId, text = "Hello, users buttons!", replyMarkup = keyboardMarkup)
            }

            callbackQuery("testButton") { bot, update ->
                update.callbackQuery?.let {
                    val chatId = it.message?.chat?.id ?: return@callbackQuery
                    bot.sendMessage(chatId = chatId, text = it.data)
                }
            }

            callbackQuery(createAlertCallbackQueryHandler { bot, update ->
                update.callbackQuery?.let {
                    val chatId = it.message?.chat?.id ?: return@createAlertCallbackQueryHandler
                    bot.sendMessage(chatId = chatId, text = it.data)
                }
            })

            text("ping") { bot, update ->
                bot.sendMessage(chatId = update.message!!.chat.id, text = "Pong")
            }

            location { bot, update, location ->
                val chatId = update.message?.chat?.id ?: return@location
                bot.sendMessage(
                    chatId = chatId,
                    text = "Your location is (${location.latitude}, ${location.longitude})",
                    replyMarkup = ReplyKeyboardRemove()
                )
            }

            contact { bot, update, contact ->
                val chatId = update.message?.chat?.id ?: return@contact
                bot.sendMessage(
                    chatId = chatId,
                    text = "Hello, ${contact.firstName} ${contact.lastName}",
                    replyMarkup = ReplyKeyboardRemove()
                )
            }

            channel { bot, update ->
                // Handle channel update
            }

            inlineQuery { bot, inlineQuery ->
                val queryText = inlineQuery.query

                if (queryText.isBlank() or queryText.isEmpty()) return@inlineQuery

                val inlineResults = (0 until 5).map {
                    InlineQueryResult.Article(
                        id = it.toString(),
                        title = "$it. $queryText",
                        inputMessageContent = InputMessageContent.Text("$it. $queryText"),
                        description = "Add $it. before you word"
                    )
                }
                bot.answerInlineQuery(inlineQuery.id, inlineResults)
            }

            photos { bot, update, _ ->
                val chatId = update.message?.chat?.id ?: return@photos
                bot.sendMessage(
                    chatId = chatId,
                    text = "Wowww, awesome photos!!! :P"
                )
            }

            telegramError { _, telegramError ->
                println(telegramError.getErrorMessage())
            }
        }
    }

    bot.startPolling()
}

fun generateUsersButton(): List<List<KeyboardButton>> {
    return listOf(
        listOf(KeyboardButton("Request location (not supported on desktop)", requestLocation = true)),
        listOf(KeyboardButton("Request contact", requestContact = true))
    )
}

fun createAlertCallbackQueryHandler(handler: HandleUpdate): CallbackQueryHandler {
    return CallbackQueryHandler(
        callbackData = "showAlert",
        callbackAnswerText = "HelloText",
        callbackAnswerShowAlert = true,
        handler = handler
    )
}

fun generateButtons(): List<List<InlineKeyboardButton>> {
    return listOf(
        listOf(InlineKeyboardButton(text = "Test Inline Button", callbackData = "testButton")),
        listOf(InlineKeyboardButton(text = "Show alert", callbackData = "showAlert"))
    )
}

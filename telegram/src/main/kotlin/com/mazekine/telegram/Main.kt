package com.mazekine.telegram

import com.mazekine.client.apis.ProfileApi
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
import com.mazekine.telegram.entities.payments.InvoicePhotoDetail
import com.mazekine.telegram.extensions.filters.Filter
import com.mazekine.telegram.extensions.mazekine.MazekineClient
import com.mazekine.telegram.logics.entities.BotContext
import com.mazekine.telegram.network.fold
import com.mazekine.telegram.types.ContextButtonsContext
import com.mazekine.telegram.types.ContextButtonsContext.ACCOUNT
import com.mazekine.telegram.types.ContextButtonsContext.CHECK_ADDRESS
import com.mazekine.telegram.types.ContextButtonsContext.MY_ADDRESSES
import com.mazekine.telegram.types.ContextButtonsContext.START
import com.mazekine.telegram.types.InlineButtonsContext
import com.mazekine.telegram.types.InlineButtonsContext.TICKER_SELECTION
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
                var txt: kotlin.String
                val token: kotlin.String

                val objMazekineClient: MazekineClient = MazekineClient(
                    "https://api.mazekine.com",
                    strSecret
                )

                txt = objMazekineClient?.getAuthToken()

                val addressData = objMazekineClient?.getAddressData(
                    "1Sazfo21GfN2umKyBh2mPU53YC8QKzshf", "BTC"
                )
                with(addressData){
                    txt = "*Address owner:* " + this?.owner?.firstName + " " + this?.owner?.lastName +
                        "\n*Wallet name:* " + this?.wallet?.name +
                        "\n*Transaction ID:* " + this?.transaction?.id +
                        "\n*Transactions available: *" + this?.transaction?.requestsAvailable
                }

                var result = bot.sendMessage(
                    chatId = update.message!!.chat.id,
                    text = "Got some data for you!\n\n$txt",
                    parseMode = MARKDOWN
                )

                result.fold({

                }, {

                })
            }

            command("start") { bot, update ->

                val chatId = update.message?.chat?.id ?: return@command

                val keyboardMarkup = KeyboardReplyMarkup(keyboard = generateUsersButton(START), resizeKeyboard = true)
                bot.sendMessage(
                    chatId = chatId,
                    text = "Welcome to Mazekine!\n\n" +
                           "Tap *Check address* button below to get details on any blockchain address \uD83D\uDC47",
                    replyMarkup = keyboardMarkup,
                    parseMode = MARKDOWN)

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

            text("\uD83D\uDD0D Check address"){bot, update ->
                val chatId = update.message?.chat?.id ?: return@text

                //val keyboardMarkup = KeyboardReplyMarkup(keyboard = generateUsersButton(CHECK_ADDRESS), resizeKeyboard = true)
                val inlineKeyboardMarkup = InlineKeyboardMarkup(generateInlineButtons(TICKER_SELECTION))
                bot.sendMessage(
                    chatId = chatId,
                    text = "Select coin ticker from the list or type it in the field below \uD83D\uDC47",
                    replyMarkup = inlineKeyboardMarkup)
            }

            callbackQuery("TickerSelector" ) { bot, update ->
                update.callbackQuery?.let {
                    val chatId = it.message?.chat?.id ?: return@callbackQuery
                    bot.sendMessage(chatId = chatId, text = it.data + "\n" + it.message)
                }
            }


            text("↩️ Back to main screen"){bot, update ->
                val chatId = update.message?.chat?.id ?: return@text

                val keyboardMarkup = KeyboardReplyMarkup(keyboard = generateUsersButton(START), resizeKeyboard = true)
                bot.sendMessage(
                    chatId = chatId,
                    text = "Tap Check address button below to get details on any blockchain address \uD83D\uDC47",
                    replyMarkup = keyboardMarkup)
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

                bot.sendMessage(
                    chatId = update.message!!.chat.id,
                    text = "Something happened"
                )
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


fun generateInlineButtons(context: InlineButtonsContext = TICKER_SELECTION): List<List<InlineKeyboardButton>> {
    val result: List<List<InlineKeyboardButton>>

    result = when(context){
        TICKER_SELECTION -> listOf(
            listOf(
                InlineKeyboardButton("BTC", callbackData = "TickerSelector"),
                InlineKeyboardButton("ETH", callbackData = "TickerSelector"),
                InlineKeyboardButton("XRP", callbackData = "TickerSelector")
            ),
            listOf(
                InlineKeyboardButton("USDT", callbackData = "TickerSelector"),
                InlineKeyboardButton("BCH", callbackData = "TickerSelector"),
                InlineKeyboardButton("LTC", callbackData = "TickerSelector")
            ),
            listOf(
                InlineKeyboardButton("EOS", callbackData = "TickerSelector"),
                InlineKeyboardButton("BNB", callbackData = "TickerSelector"),
                InlineKeyboardButton("BSV", callbackData = "TickerSelector")
            )
        )
    }

    return result
}

fun generateUsersButton(context: ContextButtonsContext = START): List<List<KeyboardButton>> {

    val result: List<List<KeyboardButton>>

    result = when(context){
        START -> listOf(
            listOf(
                KeyboardButton("🔍 Check address")
            ),
            listOf(
                KeyboardButton("My addresses"),
                KeyboardButton("Account")
            )
        )
        CHECK_ADDRESS, MY_ADDRESSES, ACCOUNT -> listOf(
            listOf(
                KeyboardButton("↩️ Back to main screen")
            )
        )
        else -> TODO()
    }

    return result
    /*listOf(
        listOf(KeyboardButton("Request location (not supported on desktop)", requestLocation = true)),
        listOf(KeyboardButton("Request contact", requestContact = true))
    )*/
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

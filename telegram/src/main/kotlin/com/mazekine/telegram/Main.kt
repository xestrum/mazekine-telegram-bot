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
import com.natpryce.konfig.*
import com.natpryce.konfig.ConfigurationProperties.Companion
import java.io.File
import com.mazekine.telegram.configuration.*
import com.mazekine.telegram.logics.cryptocurrencies.KnownCoins
import com.mazekine.telegram.types.Cryptocurrency
import com.mazekine.telegram.types.ExpectedInput
import com.mazekine.telegram.types.ExpectedInput.BLOCKCHAIN_ADDRESS
import com.mazekine.telegram.types.ExpectedInput.COIN_TICKER
import com.mazekine.telegram.types.ExpectedInput.NONE
import java.lang.Exception
import java.util.logging.Level
import java.util.logging.Logger
import kotlin.text.RegexOption.IGNORE_CASE

fun main(args: Array<String>) {

    val config = ConfigurationProperties.systemProperties() overriding
                EnvironmentVariables() overriding
                ConfigurationProperties.fromFile(
                    File(
                        "telegram/src/main/kotlin/com/mazekine/telegram/configuration/defaults.properties"
                    )
                )

    var expectedInput: MutableMap<String, ExpectedInput> = mutableMapOf()
    expectedInput.logger().level = Level.INFO

    var currentCoin: String = ""
    var currentAddress: String = ""

    //  Initialize list of known coins
    try{
        KnownCoins.initialized
    }catch(e: Exception){
        println(e.message)
    }

    //  Initialize Mazekine connection
    val mazekineClient = MazekineClient(
        config[MazekineConfig.api_address].toString(),
        config[MazekineConfig.api_secret]
    )

    val bot = bot {

        token = config[TelegramConfig.token]
        timeout = config[TelegramConfig.timeout]
        logLevel = HttpLoggingInterceptor.Level.BODY

        dispatch {
/*
            message(Filter.Sticker) { bot, update ->
                bot.sendMessage(update.message!!.chat.id, text = "You have received an awesome sticker \\o/")
            }

            message(Filter.Reply or Filter.Forward) { bot, update ->
                bot.sendMessage(update.message!!.chat.id, text = "someone is replying or forwarding messages ...")
            }
*/

            text(null) {bot, update ->
                val chatId = update.message?.chat?.id ?: return@text

                when(expectedInput[update.message.from?.username ?: ""]){
                    COIN_TICKER -> let{
                        val coin = update.message.text ?: return@let
                        if (KnownCoins.contains(coin)) {
                            bot.sendMessage(
                                chatId = chatId,
                                text = "You have selected *" +
                                        (KnownCoins[coin]?.name ?: coin) +
                                        "* \uD83D\uDC4D" +
                                        "\n\nPlease input the address to check in the field below.",
                                parseMode = MARKDOWN,
                                replyMarkup = KeyboardReplyMarkup(
                                    generateUsersButton(CHECK_ADDRESS),
                                    resizeKeyboard = true
                                )
                            )

                            currentCoin = coin.toUpperCase().trim()
                            expectedInput[update.message.from?.username ?: ""] = BLOCKCHAIN_ADDRESS
                            expectedInput.logger().info(expectedInput.toString())
                        } else {
                            bot.sendMessage(
                                chatId = chatId,
                                text = "\uD83E\uDD37\u200D♂️ I don't know such coin.\n\nSelect the one from the list or input correct ticker.",
                                parseMode = MARKDOWN,
                                replyMarkup = InlineKeyboardMarkup(
                                    generateInlineButtons(
                                        TICKER_SELECTION
                                    )
                                )
                            )

                            expectedInput[update.message.from?.username ?: ""] = COIN_TICKER
                            expectedInput.logger().info(expectedInput.toString())
                        }

                        return@text
                    }

                    BLOCKCHAIN_ADDRESS -> let{
                        currentAddress = update.message.text ?: return@let
                        val chatId = update.message.chat.id
                        val keyboardMarkup = KeyboardReplyMarkup(keyboard = generateUsersButton(START), resizeKeyboard = true)

                        if(currentCoin == ""){
                            bot.sendMessage(
                                chatId = chatId,
                                text = "Oops! Something went wrong, try again.\n\n" +
                                        "Tap *\uD83D\uDD0D Check address* button below to get details on any blockchain address \uD83D\uDC47",
                                replyMarkup = keyboardMarkup,
                                parseMode = MARKDOWN)

                            currentAddress = ""
                            expectedInput[update.message.from?.username ?: ""] = NONE
                            expectedInput.logger().info(expectedInput.toString())
                            return@let
                        }

                        val addressData = mazekineClient.getAddressData(
                            currentAddress,
                            currentCoin
                        )

                        var reply: String = "\uD83E\uDD37\u200D♂️*Address not found*\n\n" +
                                "Tap *\uD83D\uDD0D Check address* button below to get details on another blockchain address \uD83D\uDC47"

                        addressData?.let{
                            reply = "✅ Address found\n\n" +
                                    "The ${KnownCoins[currentCoin]?.name ?: ""} address *$currentAddress* belongs to " +
                                    "*${it.owner?.firstName ?: ""} ${it.owner?.lastName ?: ""}* and is operated by " +
                                    "*${it.wallet?.name ?: ""}*"
                        }

                        bot.sendMessage(
                            chatId = update.message.chat.id,
                            text = reply,
                            replyMarkup = keyboardMarkup,
                            parseMode = MARKDOWN
                        )

                        currentAddress = ""
                        currentCoin = ""
                        expectedInput[update.message.from?.username ?: ""] = NONE
                        expectedInput.logger().info(expectedInput.toString())

                        return@text
                    }

                    NONE -> let{
                        return@text
                    }
                }
            }

            command("check") { bot, update ->

                val strSecret: String = config[MazekineConfig.api_secret]
                val objProfile: ProfileApi = ProfileApi()
                var txt: kotlin.String
                val token: kotlin.String

                val objMazekineClient: MazekineClient = MazekineClient(
                    config[MazekineConfig.api_address].toString(),
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
                           "Tap *\uD83D\uDD0D Check address* button below to get details on any blockchain address \uD83D\uDC47",
                    replyMarkup = keyboardMarkup,
                    parseMode = MARKDOWN)

                expectedInput[update.message.from?.username ?: ""] = NONE
                expectedInput.logger().info(expectedInput.toString())
            }

/*
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
*/

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

                bot.sendMessage(
                    chatId = chatId,
                    text = "\uD83D\uDC4C Perfect, let's do it.",
                    replyMarkup = KeyboardReplyMarkup(
                        generateUsersButton(CHECK_ADDRESS),
                        resizeKeyboard = true)
                )

                bot.sendMessage(
                    chatId = chatId,
                    text = "Select any coin ticker from the list below \uD83D\uDC47",
                    replyMarkup = InlineKeyboardMarkup(
                        generateInlineButtons(TICKER_SELECTION)
                    )
                )

                expectedInput[update.message.from?.username ?: ""] = COIN_TICKER
                expectedInput.logger().info(expectedInput.toString())
            }

            callbackQuery("TickerSelector" ) { bot, update ->
                update.callbackQuery?.let {
                    val chatId = it.message?.chat?.id ?: return@callbackQuery

                    val commandValidityPattern: Regex = """^TickerSelector\[([a-zA-Z0-9]+)\]$""".toRegex(IGNORE_CASE)

                    if(commandValidityPattern.matches(it.data)){
                        val coin = commandValidityPattern.find(it.data)!!.groups[1]!!.value
                        coin?.let {
                            if(KnownCoins.contains(coin)) {
                                bot.sendMessage(
                                    chatId = chatId,
                                    text = "You have selected *" +
                                            (KnownCoins[coin]?.name ?: coin) +
                                            "* \uD83D\uDC4D" +
                                            "\n\nPlease input the address to check in the field below.",
                                    parseMode = MARKDOWN,
                                    replyMarkup = KeyboardReplyMarkup(
                                        generateUsersButton(CHECK_ADDRESS),
                                        resizeKeyboard = true
                                    )
                                )

                                currentCoin = coin.toUpperCase().trim()
                                expectedInput[update.callbackQuery.from.username ?: ""] = BLOCKCHAIN_ADDRESS
                                expectedInput.logger().info(expectedInput.toString())
                            } else {
                                bot.sendMessage(
                                    chatId = chatId,
                                    text = "\uD83E\uDD37\u200D♂️ I don't know such coin.\n\nSelect the one from the list or input correct ticker.",
                                    parseMode = MARKDOWN,
                                    replyMarkup = InlineKeyboardMarkup(generateInlineButtons(TICKER_SELECTION))
                                )

                                expectedInput[update.callbackQuery.from.username ?: ""] = COIN_TICKER
                                expectedInput.logger().info(expectedInput.toString())
                            }
                        }
                    } else {
                        bot.sendMessage(
                            chatId = chatId,
                            text = "\uD83E\uDD37\u200D♂️I don't know such coin.\n\nSelect the one from the list.",
                            parseMode = MARKDOWN,
                            replyMarkup = InlineKeyboardMarkup(generateInlineButtons(TICKER_SELECTION))
                        )

                        expectedInput[update.callbackQuery.from.username ?: ""] = COIN_TICKER
                        expectedInput.logger().info(expectedInput.toString())
                    }
                }
            }


            text("↩️ Back to main screen"){bot, update ->
                val chatId = update.message?.chat?.id ?: return@text

                val keyboardMarkup = KeyboardReplyMarkup(keyboard = generateUsersButton(START), resizeKeyboard = true)
                bot.sendMessage(
                    chatId = chatId,
                    text = "Tap *\uD83D\uDD0D Check address* button below to get details on any blockchain address \uD83D\uDC47",
                    replyMarkup = keyboardMarkup,
                    parseMode = MARKDOWN)

                expectedInput[update.message.from?.username ?: ""] = NONE
                expectedInput.logger().info(expectedInput.toString())
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
                InlineKeyboardButton("BTC", callbackData = "TickerSelector[BTC]"),
                InlineKeyboardButton("ETH", callbackData = "TickerSelector[ETH]"),
                InlineKeyboardButton("XRP", callbackData = "TickerSelector[XRP]")
            ),
            listOf(
                InlineKeyboardButton("USDT", callbackData = "TickerSelector[USDT]"),
                InlineKeyboardButton("BCH", callbackData = "TickerSelector[BCH]"),
                InlineKeyboardButton("LTC", callbackData = "TickerSelector[LTC]")
            ),
            listOf(
                InlineKeyboardButton("EOS", callbackData = "TickerSelector[EOS]"),
                InlineKeyboardButton("BNB", callbackData = "TickerSelector[BNB]"),
                InlineKeyboardButton("BSV", callbackData = "TickerSelector[BSV]")
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

inline fun <reified T : Any> T.logger(): Logger =
    Logger.getLogger(T::class.java.name)
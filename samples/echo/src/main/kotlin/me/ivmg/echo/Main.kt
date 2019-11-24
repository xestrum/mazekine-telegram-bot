package me.ivmg.echo

import com.mazekine.telegram.bot
import com.mazekine.telegram.dispatch
import com.mazekine.telegram.dispatcher.text

fun main(args: Array<String>) {

    val bot = bot {

        token = "YOUR_API_KEY"

        dispatch {

            text { bot, update ->
                val text = update.message?.text ?: "Hello, World!"
                bot.sendMessage(chatId = update.message!!.chat.id, text = text)
            }
        }
    }

    bot.startPolling()
}

package com.mazekine.telegram.logics.entities

import com.mazekine.telegram.Bot
import com.mazekine.telegram.entities.KeyboardButton
import com.sun.org.apache.xpath.internal.operations.Bool
import java.util.UUID
import java.util.function.Consumer
import kotlin.math.max
import kotlin.text.trim


class BotContext private constructor(
    bCaption: String? = null,
    bParent: BotContext? = null
) {
    /**
     * Unique identifier of the context item
     */
    val id: UUID = UUID.randomUUID()

    /**
     * Text displayed on the context button
     */
    var caption: String = bCaption ?: "[CONTEXT BUTTON]"
        set(value) {
            if("" == value.trim()){
                field = "[CONTEXT BUTTON]"
            } else {
                field = value.trim()
            }
        }

    /**
     * Attach specific handler to the current instance
     */
    var handler: Any? = TODO()

    /**
     * Parent object of the current instance
     */
    var parent: BotContext? = bParent

    /**
     * Rows of KeyboardButtons
     */
    var rows: MutableList<MutableList<BotContext>> = mutableListOf(mutableListOf())
        private set

    /**
     * Adds new row to collection
     * @param [items] BotContext objects
     */
    fun addRow(items: MutableList<BotContext>?): Unit {
        this.rows.add(
            items ?: mutableListOf<BotContext>()
        )
    }

    /**
     * Removes a row from the collection
     * @param [index] Index of the row
     * @return [Boolean] True if removed successfully, false if an error has occurred
     */
    fun removeRow(index: Int): Boolean {
        try {
            this.rows.removeAt(index)
        } catch (e: Error) {
            println("Row removal error at position $index")
            return false
        }

        return true
    }
    
    operator fun get(index: Int): MutableList<BotContext>? {
        return this.rows[index]
    }
}
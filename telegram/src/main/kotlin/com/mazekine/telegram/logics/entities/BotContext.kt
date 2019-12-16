package com.mazekine.telegram.logics.entities

import com.mazekine.telegram.Bot
import com.mazekine.telegram.entities.KeyboardButton
import com.sun.org.apache.xpath.internal.operations.Bool
import java.util.function.Consumer
import kotlin.math.max

/**
 * @property [contextName] Symbolic name of the context in Telegram bot
 * @property [children] Nested context items
 * @property [parent] Parent context object
 * @property [root] Root object of the tree
 * @constructor Create object to
 */
public class BotContext private constructor(
    val contextName: String,
    val parent: BotContext? = null,
    var children: MutableList<BotContext>? = null,
    val root: BotContext? = null,
    var row: Int = 1
) {
    /**
     * Quantity of rows in the object
     */
    var rowsCount: Int = 0
        get() = getRowsNumber()

    /**
     * Number of row at which the object is located
     */
    var rowNumber: Int = row
        get() = field
        set(value) {
            field = value
        }

    /**
     * @param [contextName] Symbolic name of the context in Telegram bot
     */
    constructor(
        contextName: String
    ) : this(
        contextName,
        null,
        null,
        null,
        1)

    /**
     * @param [contextName] Symbolic name of the context in Telegram bot
     * @param [parent] Parent context object
     */
    constructor(
        contextName: String,
        parent: BotContext?,
        row: Int?
    ) : this(
        contextName,
        parent,
        null,
        parent?.root ?: parent,
        row ?: 1
    )

    /**
     * @param [contextName] Symbolic name of the context in Telegram bot
     * @param [children] Nested context items
     * @param [parent] Parent context object
     * @param [root] Root object of the tree
     */
    constructor(
        contextName: String,
        children: MutableList<BotContext>?,
        parent: BotContext?,
        row: Int?
    ) : this(
        contextName,
        parent,
        children,
        parent?.root ?: parent,
        row ?: 1
    )

    /**
     * @param [contextName] Name of context item to add
     * @param [children] Mutable list of children to add to created item
     * @param [unique] Specify if the item should be unique in this context to avoid duplicates
     * @return Boolean True is item was added or false it is duplicated
     */
    fun addChild(
        contextName: String,
        children: MutableList<BotContext>?,
        unique: Boolean = false,
        row: Int? = null
    ) : BotContext {
        //  Create item to add
        val child = BotContext(
            contextName,
            children,
            this,
            this.row
        )

        when(this.children){
            null -> {
                this.children = mutableListOf(child)
                return this.children!!.first()
            }
            else -> {
                if(unique){
                    if(this.children!!.contains(child)) {
                        return this.children!!.elementAt(
                            this.children!!.indexOf(child)
                        )
                    }
                }

                this.children?.add(child)
                return this.children!!.last()
            }
        }
    }

    private fun getRowsNumber() : Int {
        var result: Int = 0

        this.children?.let {
            it.forEach(){
                result = max(it.rowNumber, result)
            }
        }

        return result
    }

    fun getChildButtons(): List<List<KeyboardButton>>? {
        

        return null
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as BotContext

        if (contextName != other.contextName) return false
        if (children != other.children) return false
        if (parent != other.parent) return false
        if (root != other.root) return false
        if (row != other.row) return false

        return true
    }

    override fun hashCode(): Int {
        var result = contextName.hashCode()
        result = 31 * result + (children?.hashCode() ?: 0)
        result = 31 * result + (parent?.hashCode() ?: 0)
        result = 31 * result + (root?.hashCode() ?: 0)
        result = 31 * result + (row.hashCode() ?: 0)
        return result
    }

}
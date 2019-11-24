package com.mazekine.telegram.dispatcher.handlers

import com.mazekine.telegram.HandleUpdate
import com.mazekine.telegram.entities.Update

abstract class Handler(val handlerCallback: HandleUpdate) {
    abstract val groupIdentifier: String

    abstract fun checkUpdate(update: Update): Boolean
}

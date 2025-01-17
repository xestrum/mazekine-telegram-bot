package com.mazekine.telegram.entities.inputmedia

abstract class InputMedia {
    abstract val type: String
    abstract val media: String
    abstract val caption: String?
}

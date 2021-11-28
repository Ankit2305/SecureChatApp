package com.ankitkumar.securechatapplication.model

import java.util.*

data class Message(
    val id: UUID,
    val text: String,
    var received: Boolean = true
//    val sender: String,
//    val date: String,
//    val time: String
) {
    fun setAsSendMessage() {
        received = false
    }
}

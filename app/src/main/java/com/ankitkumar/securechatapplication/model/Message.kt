package com.ankitkumar.securechatapplication.model

import androidx.constraintlayout.widget.ConstraintSet
import com.ankitkumar.securechatapplication.util.MessageType
import java.util.*


data class Message(
    val type: Int = MessageType.MESSAGE,
    val id: UUID,
    val text: String,
    var received: Boolean = true,
    val to : String,  // TODO  : rename it to "receiver"
//    val sender: String,
//    val date: String,
//    val time: String
) {
    fun setAsSendMessage() {
        received = false
    }
}

data class Auth(
    val type: Int = MessageType.AUTH,
    val authCode : String
)
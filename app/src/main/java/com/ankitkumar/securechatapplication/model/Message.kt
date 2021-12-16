package com.ankitkumar.securechatapplication.model

import androidx.constraintlayout.widget.ConstraintSet
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.ankitkumar.securechatapplication.util.MessageType
import java.util.*

@Entity(tableName = "message")
data class Message(
    val type: Int = MessageType.MESSAGE,
    @PrimaryKey
    val id: String,
    val text: String,
    var received: Boolean = true,
    val to : String,  // TODO  : rename it to "receiver"
    val sender: String,
    val timeStamp: Long = System.currentTimeMillis()
) {
    fun setAsSendMessage() {
        received = false
    }
}

data class Auth(
    val type: Int = MessageType.AUTH,
    val authCode : String
)
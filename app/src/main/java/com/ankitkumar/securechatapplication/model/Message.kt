package com.ankitkumar.securechatapplication.model

import androidx.constraintlayout.widget.ConstraintSet
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.ankitkumar.securechatapplication.crypto.AES
import com.ankitkumar.securechatapplication.network.GsonHelper
import com.ankitkumar.securechatapplication.util.MessageType
import java.util.*

@Entity(tableName = "message")
data class Message(
    val type: Int = MessageType.MESSAGE,
    @PrimaryKey
    val id: String,
    val text: String,
    var received: Boolean = true,
    val to : String,  // TODO  : rename it to "receiver" -- don't change, changing to receiver may lead to bugs
    val sender: String,
    val senderName: String,
    val isGroupMessage: Boolean,
    val timeStamp: Long = System.currentTimeMillis()
) {
    fun setAsSendMessage() {
        received = false
    }

    fun encryptMessage() : EncryptedMessage{
        val encryptedMessage = AES.encrypt(GsonHelper.getJson(this)) ?: ""
        return EncryptedMessage(
            to = to,
            type = type,
            encryptedData = encryptedMessage
        )
    }

}

data class Auth(
    val type: Int = MessageType.AUTH,
    val authCode : String
)
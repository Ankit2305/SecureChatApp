package com.ankitkumar.securechatapplication.model

import com.ankitkumar.securechatapplication.crypto.AES
import com.ankitkumar.securechatapplication.network.GsonHelper
import com.ankitkumar.securechatapplication.util.MessageType

data class EncryptedMessage(
    val type: Int = MessageType.MESSAGE,
    val to: String,
    val encryptedData: String
) {
    fun toMessage(): Message? {
        val json = AES.decrypt(encryptedData) ?: ""
        return GsonHelper.getMessageFromJson(json)
    }
}
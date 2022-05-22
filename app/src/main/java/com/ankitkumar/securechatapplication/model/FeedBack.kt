package com.ankitkumar.securechatapplication.model

import com.ankitkumar.securechatapplication.util.MessageType

data class FeedBack(
    val chatId: String,
    val type: Int = MessageType.FEEDBACK
)
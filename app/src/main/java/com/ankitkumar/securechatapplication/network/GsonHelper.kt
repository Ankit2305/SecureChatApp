package com.ankitkumar.securechatapplication.network

import com.ankitkumar.securechatapplication.model.Message
import com.google.gson.Gson
import java.lang.Exception

object GsonHelper {
    private val gson = Gson()

    fun getMessageFromJson(json: String): Message? {
        return try {
            gson.fromJson(json, Message::class.java)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    fun getJsonFromMessage(message: Message): String {
        return gson.toJson(message)
    }
}
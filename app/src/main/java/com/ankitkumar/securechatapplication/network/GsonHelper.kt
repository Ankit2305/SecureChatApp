package com.ankitkumar.securechatapplication.network

import com.ankitkumar.securechatapplication.model.EncryptedMessage
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

    fun getEncryptedMessageFromJson(json: String): EncryptedMessage? {
        return try {
            gson.fromJson(json, EncryptedMessage::class.java)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    fun getJson(objects: Any): String {
        return gson.toJson(objects)
    }

}
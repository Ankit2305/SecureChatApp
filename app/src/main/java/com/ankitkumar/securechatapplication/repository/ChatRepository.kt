package com.ankitkumar.securechatapplication.repository

import androidx.lifecycle.LiveData
import com.ankitkumar.securechatapplication.db.ChatDatabase
import com.ankitkumar.securechatapplication.model.Message
import kotlinx.coroutines.*

class ChatRepository(val database: ChatDatabase) {

    suspend fun getMessagesForSender(senderId: String): LiveData<List<Message>> {
        lateinit var messages: LiveData<List<Message>>
        coroutineScope {
            withContext(Dispatchers.IO) {
                messages = database.messageDao().getMessages(senderId)
            }
        }
        return messages
    }

    suspend fun insertMessage(message: Message) {
        withContext(Dispatchers.IO) {
            database.messageDao().insertMessage(message)
        }
    }

    suspend fun getMessageCount(userId: String): LiveData<Int> {
        var messageCount = 0
        coroutineScope {
            withContext(Dispatchers.IO) {
                messageCount = database.messageDao().getMessageCount(userId)
            }
        }
    }

}
package com.ankitkumar.securechatapplication.repository

import androidx.lifecycle.LiveData
import com.ankitkumar.securechatapplication.db.ChatDatabase
import com.ankitkumar.securechatapplication.model.GroupDetail
import com.ankitkumar.securechatapplication.model.Message
import com.ankitkumar.securechatapplication.network.ChatApiService
import kotlinx.coroutines.*

class ChatRepository(val database: ChatDatabase, val chatApiService: ChatApiService) {

    suspend fun getMessagesForSender(senderId: String, isGroupChat: Boolean): LiveData<List<Message>> {
        lateinit var messages: LiveData<List<Message>>
        coroutineScope {
            withContext(Dispatchers.IO) {
                messages = database.messageDao().getMessages(senderId, isGroupChat)
            }
        }
        return messages
    }

    suspend fun insertMessage(message: Message) {
        withContext(Dispatchers.IO) {
            if(database.messageDao().containsMessage(message.id) == 0) {
                database.messageDao().insertMessage(message)
            }
        }
    }

    suspend fun getMessageCount(userId: String): LiveData<Int> {
        lateinit var messageCount: LiveData<Int>
        coroutineScope {
            withContext(Dispatchers.IO) {
                messageCount = database.messageDao().getMessageCount(userId)
            }
        }
        return messageCount
    }

    suspend fun getGroupById(groupId: String): GroupDetail {
         return chatApiService.fetchGroupById(groupId)
    }

}
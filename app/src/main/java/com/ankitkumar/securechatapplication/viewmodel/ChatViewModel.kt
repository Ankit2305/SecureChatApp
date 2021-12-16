package com.ankitkumar.securechatapplication.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ankitkumar.securechatapplication.model.Message
import com.ankitkumar.securechatapplication.network.WebSocketHelper
import com.ankitkumar.securechatapplication.network.WebSocketWrapper
import com.ankitkumar.securechatapplication.repository.ChatRepository
import kotlinx.coroutines.runBlocking

class ChatViewModel(val repository: ChatRepository, val webSocket: WebSocketWrapper): ViewModel() {
    fun sendMessage(message: Message) {
        webSocket.send(message)
    }

    fun getMessagesWithUser(userId: String) = runBlocking {
        repository.getMessagesForSender(userId)
    }
}
package com.ankitkumar.securechatapplication.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ankitkumar.securechatapplication.model.GroupDetail
import com.ankitkumar.securechatapplication.model.Message
import com.ankitkumar.securechatapplication.network.WebSocketHelper
import com.ankitkumar.securechatapplication.network.WebSocketWrapper
import com.ankitkumar.securechatapplication.repository.ChatRepository
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class ChatViewModel(val repository: ChatRepository, val webSocket: WebSocketWrapper): ViewModel() {
    val groupDetailLiveData = MutableLiveData<GroupDetail>()
    fun sendMessage(message: Message) {
        webSocket.send(message)
    }

    fun getMessagesWithUser(userId: String, isGroupChat: Boolean) = runBlocking {
        repository.getMessagesForSender(userId, isGroupChat)
    }

    fun fetchGroupById(groupId: String) {
        viewModelScope.launch {
            groupDetailLiveData.postValue(repository.getGroupById(groupId))
        }
    }
}
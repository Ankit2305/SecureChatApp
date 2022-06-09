package com.ankitkumar.securechatapplication.viewmodel

import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ankitkumar.securechatapplication.model.GroupDetail
import com.ankitkumar.securechatapplication.model.Message
import com.ankitkumar.securechatapplication.network.WebSocketHelper
import com.ankitkumar.securechatapplication.network.WebSocketWrapper
import com.ankitkumar.securechatapplication.repository.ChatRepository
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.util.*

class ChatViewModel(val repository: ChatRepository, val webSocket: WebSocketWrapper): ViewModel() {
    val groupDetailLiveData = MutableLiveData<GroupDetail>()
    var storageReference: StorageReference? = FirebaseStorage.getInstance().reference
    val uploadInProgress = MutableLiveData<Boolean>()
    val imageDownloadLink = MutableLiveData<String>()

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

    fun uploadFile(uri: Uri) {
        storageReference?.let { reference ->
            val imageRef: StorageReference = reference.child("images/IMG_${System.currentTimeMillis()}_${UUID.randomUUID()}.jpg")
            uploadInProgress.postValue(true)
            val uploadTask = imageRef.putFile(uri)
            val urlTask = uploadTask.continueWithTask { task ->
                if (!task.isSuccessful) {
                    task.exception?.let {
                        throw it
                    }
                }
                imageRef.downloadUrl
            }.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    imageDownloadLink.postValue(task.result.toString())
                    Log.i("DebugTag", "uploadFile: ${task.result}")
                } else {
                    imageDownloadLink.postValue("")
                    task.exception?.printStackTrace()
                    Log.i("DebugTag", "uploadFile: Failed")
                }
                uploadInProgress.postValue(false)
            }
        }
    }
}
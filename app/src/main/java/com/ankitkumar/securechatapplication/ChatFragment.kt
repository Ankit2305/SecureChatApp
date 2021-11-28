package com.ankitkumar.securechatapplication

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.ankitkumar.securechatapplication.adapter.ChatAdapter
import com.ankitkumar.securechatapplication.databinding.FragmentChatBinding
import com.ankitkumar.securechatapplication.model.Message
import com.ankitkumar.securechatapplication.network.GsonHelper
import com.ankitkumar.securechatapplication.network.WebSocketHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import java.util.*
import kotlin.collections.ArrayList

class ChatFragment : Fragment(R.layout.fragment_chat) {

    lateinit var bindings: FragmentChatBinding
    lateinit var webSocket: WebSocketHelper
    lateinit var chatAdapter: ChatAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindings = FragmentChatBinding.bind(view)
        webSocket = WebSocketHelper(object : WebSocketListener() {
            override fun onOpen(webSocket: WebSocket, response: Response) {
                showToast("Connected...")
            }

            override fun onMessage(webSocket: WebSocket, json: String) {
                showToast(json)
                handleMessage(json)
            }

            override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
                showToast("Failed...")
                t.printStackTrace()
            }
        }, requireContext())
        chatAdapter = ChatAdapter()

        lifecycleScope.launch {
            webSocket.connect()
        }

        bindings.apply {
            chatRecyclerView.setHasFixedSize(true)
            chatRecyclerView.adapter = chatAdapter

            sendButton.setOnClickListener {
                val messageText = messageEditText.text.toString().trim()
                messageEditText.setText("")
                if(messageText.isNotBlank()) {
                    val message = Message(
                        UUID.randomUUID(),
                        messageText,
                        true
                    )
                    sendMessage(message)
                }
            }
        }

    }

    private fun handleMessage(json: String) {
        val message = GsonHelper.getMessageFromJson(json)
        message?.let {
            val messages = ArrayList(chatAdapter.currentList)
            messages.add(message)
            chatAdapter.submitList(messages)
        }
    }

    private fun sendMessage(message: Message) {
        val messageJson = GsonHelper.getJsonFromMessage(message)
        webSocket.send(messageJson)
        message.setAsSendMessage()
        val messages = ArrayList(chatAdapter.currentList)
        messages.add(message)
        chatAdapter.submitList(messages)
    }

    fun showToast(message: String) {
        lifecycleScope.launch(Dispatchers.Main) {
            Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
        }
    }

}
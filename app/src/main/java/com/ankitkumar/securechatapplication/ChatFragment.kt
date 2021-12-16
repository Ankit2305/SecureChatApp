package com.ankitkumar.securechatapplication

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.ankitkumar.securechatapplication.adapter.ChatAdapter
import com.ankitkumar.securechatapplication.databinding.FragmentChatBinding
import com.ankitkumar.securechatapplication.model.Auth
import com.ankitkumar.securechatapplication.model.Message
import com.ankitkumar.securechatapplication.network.GsonHelper
import com.ankitkumar.securechatapplication.network.WebSocketHelper
import com.ankitkumar.securechatapplication.util.AUTH_CODE
import com.ankitkumar.securechatapplication.util.MessageType
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
    var receiverAuth : String? = null

    companion object {
        private const val TAG = "ChatFragmentTag"
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindings = FragmentChatBinding.bind(view)

        val args by navArgs<ChatFragmentArgs>()
        val user = args.user

        bindings.toolbar.titleTextView.text = user.name
        bindings.toolbar.subTitleTextView.text = user.phoneNo
        receiverAuth = user.uid

        webSocket = WebSocketHelper(object : WebSocketListener() {
            override fun onOpen(webSocket: WebSocket, response: Response) {
                showLogs("Connected...")
            }

            override fun onMessage(webSocket: WebSocket, json: String) {
                showLogs(json)
                handleMessage(json)
            }

            override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
                showLogs("Failed...")
                t.printStackTrace()
            }
        }, requireContext())
        chatAdapter = ChatAdapter()

        lifecycleScope.launch {
            webSocket.connect()
            setUpAuthorization()
        }

        bindings.apply {
            chatRecyclerView.setHasFixedSize(true)
            chatRecyclerView.adapter = chatAdapter

            sendButton.setOnClickListener {
                val messageText = messageEditText.text.toString().trim()
                messageEditText.setText("")
                Log.d("ChatFragment"," receiver uth : $receiverAuth")
                if(messageText.isNotBlank() && receiverAuth!=null) {
                    val message = Message(
                        MessageType.MESSAGE,
                        UUID.randomUUID(),
                        messageText,
                        true,
                        receiverAuth!!
                    )
                    sendMessage(message)
                }
            }
        }

    }

    private fun setUpAuthorization() {
        val sharedPref = activity?.getSharedPreferences(resources.getString(R.string.app_name),Context.MODE_PRIVATE)
        val authCode = sharedPref?.getString(AUTH_CODE,"12345")

        if(authCode!=null){
            val authJson = Auth(
                MessageType.AUTH,
                authCode
            )
            val json = GsonHelper.getJson(authJson)
            Log.d("ChatFragment",json)
            webSocket.send(json)
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
        val messageJson = GsonHelper.getJson(message)
        webSocket.send(messageJson)
        message.setAsSendMessage()
        val messages = ArrayList(chatAdapter.currentList)
        messages.add(message)
        chatAdapter.submitList(messages)
    }

    fun showLogs(message: String) {
        Log.i(TAG, "showLogs: $message")
    }

}
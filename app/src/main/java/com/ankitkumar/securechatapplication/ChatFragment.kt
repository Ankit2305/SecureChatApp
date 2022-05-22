package com.ankitkumar.securechatapplication

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.ankitkumar.securechatapplication.adapter.ChatAdapter
import com.ankitkumar.securechatapplication.databinding.FragmentChatBinding
import com.ankitkumar.securechatapplication.model.Auth
import com.ankitkumar.securechatapplication.model.Message
import com.ankitkumar.securechatapplication.network.GsonHelper
import com.ankitkumar.securechatapplication.network.WebSocketHelper
import com.ankitkumar.securechatapplication.util.AUTH_CODE
import com.ankitkumar.securechatapplication.util.MessageType
import com.ankitkumar.securechatapplication.util.PreferenceHelper
import com.ankitkumar.securechatapplication.viewmodel.ChatViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import org.koin.android.ext.android.inject
import java.util.*
import kotlin.collections.ArrayList

class ChatFragment : Fragment(R.layout.fragment_chat) {

    lateinit var bindings: FragmentChatBinding
    lateinit var chatAdapter: ChatAdapter
    lateinit var receiverAuth : String
    val viewModel by inject<ChatViewModel>()

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

        chatAdapter = ChatAdapter()

        bindings.apply {
            chatRecyclerView.setHasFixedSize(true)
            chatRecyclerView.layoutManager = LinearLayoutManager(requireContext()).apply {
                stackFromEnd = true
            }
            chatRecyclerView.adapter = chatAdapter

            sendButton.setOnClickListener {
                val messageText = messageEditText.text.toString().trim()
                messageEditText.setText("")
                if(messageText.isNotBlank() && receiverAuth!=null) {
                    val message = Message(
                        MessageType.MESSAGE,
                        UUID.randomUUID().toString(),
                        messageText,
                        true,
                        receiverAuth!!,
                        PreferenceHelper.getAuthCode(requireContext())
                    )
                    sendMessage(message)
                }
            }
        }

        viewModel.getMessagesWithUser(receiverAuth).observe(viewLifecycleOwner) {
            val messages = ArrayList(it).apply {
                reverse()
            }
            Log.i(TAG, "onViewCreated: $messages")
            chatAdapter.submitList(messages)
        }

    }

    private fun sendMessage(message: Message) {
        viewModel.sendMessage(message)
    }

    fun showLogs(message: String) {
        Log.i(TAG, "showLogs: $message")
    }

}
package com.ankitkumar.securechatapplication

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.ankitkumar.securechatapplication.adapter.ChatAdapter
import com.ankitkumar.securechatapplication.databinding.FragmentChatBinding
import com.ankitkumar.securechatapplication.model.Message
import com.ankitkumar.securechatapplication.util.MessageType
import com.ankitkumar.securechatapplication.util.PreferenceHelper
import com.ankitkumar.securechatapplication.viewmodel.ChatViewModel
import org.koin.android.ext.android.inject
import java.util.*

class ChatFragment : Fragment(R.layout.fragment_chat) {

    lateinit var bindings: FragmentChatBinding
    lateinit var chatAdapter: ChatAdapter
    lateinit var receiverAuth : String
    val viewModel by inject<ChatViewModel>()
    var isGroupChat = false

    companion object {
        private const val TAG = "ChatFragmentTag"
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindings = FragmentChatBinding.bind(view)

        val args by navArgs<ChatFragmentArgs>()
        val user = args.user
        val group = args.group

        if(user.uid.isNotBlank()) {
            bindings.toolbar.titleTextView.text = user.name
            bindings.toolbar.subTitleTextView.text = user.phoneNo
            receiverAuth = user.uid
        }else{
            bindings.toolbar.titleTextView.text = group.name
            bindings.toolbar.subTitleTextView.text = "${group.members.size} members"
            bindings.toolbar.logoImageView.setImageResource(R.drawable.ic_group)
            receiverAuth = group.groupId
            isGroupChat = true
        }

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
                        if(isGroupChat) MessageType.GROUP_MESSAGE else MessageType.MESSAGE,
                        UUID.randomUUID().toString(),
                        messageText,
                        true,
                        receiverAuth!!,
                        if(isGroupChat) receiverAuth else PreferenceHelper.getUserId(requireContext())
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
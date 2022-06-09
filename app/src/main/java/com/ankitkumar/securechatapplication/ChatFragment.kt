package com.ankitkumar.securechatapplication

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
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
            bindings.toolbar.root.setOnClickListener {
                val action = ChatFragmentDirections.actionChatFragmentToGroupDetailFragment(group.groupId)
                findNavController().navigate(action)
            }
            receiverAuth = group.groupId
            isGroupChat = true
        }

        chatAdapter = ChatAdapter() { url ->
            val action = ChatFragmentDirections.actionChatFragmentToImagePreviewFragment().apply {
                this.url = url
            }
            findNavController().navigate(action)
        }

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
                    val message = messageBuilder(messageText)
                    sendMessage(message)
                }
            }
            addAttachMent.setOnClickListener {
                if(activity is MainActivity) {
                    val mainActivity = activity as? MainActivity
                    mainActivity?.let {
                        it.fragment = this@ChatFragment
                        it.showFileSelector()
                    }
                }

            }
        }

        viewModel.getMessagesWithUser(receiverAuth, isGroupChat).observe(viewLifecycleOwner) {
            val messages = ArrayList(it).apply {
                reverse()
            }
            Log.i(TAG, "onViewCreated: $messages")
            chatAdapter.submitList(messages)
        }



    }

    private fun messageBuilder(text: String): Message {
        return Message(
            if(isGroupChat) MessageType.GROUP_MESSAGE else MessageType.MESSAGE,
            UUID.randomUUID().toString(),
            text,
            true,
            receiverAuth!!,
            PreferenceHelper.getAuthCode(requireContext()),
            senderName = PreferenceHelper.getUserName(requireContext()),
            isGroupMessage = isGroupChat,
            isImage = false
        )
    }

    private fun sendMessage(message: Message) {
        viewModel.sendMessage(message)
    }

    fun showLogs(message: String) {
        Log.i(TAG, "showLogs: $message")
    }

    fun handleFileUri(uri: Uri?) {
        uri?.let {
            val action = ChatFragmentDirections.actionChatFragmentToImagePreviewFragment().apply {
                this.isGroupChat = this@ChatFragment.isGroupChat
                this.receiverAuth = this@ChatFragment.receiverAuth
                this.imageUri = it
            }
            findNavController().navigate(action)
        }
    }
}

fun String.toMessage(isGroupChat: Boolean, context: Context, receiverAuth: String, isImage: Boolean): Message{
    return Message(
        if(isGroupChat) MessageType.GROUP_MESSAGE else MessageType.MESSAGE,
        UUID.randomUUID().toString(),
        this,
        true,
        receiverAuth,
        PreferenceHelper.getAuthCode(context),
        senderName = PreferenceHelper.getUserName(context),
        isGroupMessage = isGroupChat,
        isImage = isImage
    )
}
package com.ankitkumar.securechatapplication.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBindings
import com.ankitkumar.securechatapplication.R
import com.ankitkumar.securechatapplication.databinding.SingleReceiveChatItemBinding
import com.ankitkumar.securechatapplication.model.Message

class ChatAdapter(val messages: List<Message>): RecyclerView.Adapter<ChatAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.single_receive_chat_item, parent, false)
        val binding = SingleReceiveChatItemBinding.bind(view)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val message = messages[position]
        holder.bindings.messageTextView.text = message.text
    }

    override fun getItemCount(): Int {
        return messages.size
    }

    override fun getItemViewType(position: Int): Int {
        return 1
    }

    inner class ViewHolder(val bindings: SingleReceiveChatItemBinding): RecyclerView.ViewHolder(bindings.root) {

    }
}
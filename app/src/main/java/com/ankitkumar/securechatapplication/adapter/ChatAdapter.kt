package com.ankitkumar.securechatapplication.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBindings
import com.ankitkumar.securechatapplication.R
import com.ankitkumar.securechatapplication.databinding.SingleReceiveChatItemBinding
import com.ankitkumar.securechatapplication.databinding.SingleSendChatItemBinding
import com.ankitkumar.securechatapplication.model.Message

class ChatAdapter:
    ListAdapter<Message, ChatAdapter.BaseViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        val layoutId = if(viewType == 1) R.layout.single_receive_chat_item else R.layout.single_send_chat_item
        val view = LayoutInflater.from(parent.context).inflate(layoutId, parent, false)

        return if(viewType == 1) {
            val binding = SingleReceiveChatItemBinding.bind(view)
            ReceiveViewHolder(binding)
        } else {
            val binding = SingleSendChatItemBinding.bind(view)
            SendViewHolder(binding)
        }
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        val message = getItem(position)
        holder.bind(message)
    }

    override fun getItemViewType(position: Int): Int {
        return if (getItem(position).received) 1 else 0
    }

    abstract class BaseViewHolder(view: View): RecyclerView.ViewHolder(view) {
        abstract fun bind(message: Message)
    }

    inner class ReceiveViewHolder(val bindings: SingleReceiveChatItemBinding): BaseViewHolder(bindings.root) {
        override fun bind(message: Message) {
            bindings.apply {
                messageTextView.text = message.text
            }
        }
    }

    inner class SendViewHolder(val bindings: SingleSendChatItemBinding): BaseViewHolder(bindings.root) {
        override fun bind(message: Message) {
            bindings.apply {
                messageTextView.text = message.text
            }
        }
    }

    companion object {
        val DIFF_CALLBACK = object: DiffUtil.ItemCallback<Message>() {
            override fun areItemsTheSame(oldItem: Message, newItem: Message): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Message, newItem: Message): Boolean {
                return oldItem == newItem
            }
        }
    }
}
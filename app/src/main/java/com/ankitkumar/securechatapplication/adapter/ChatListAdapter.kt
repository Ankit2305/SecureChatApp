package com.ankitkumar.securechatapplication.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ankitkumar.securechatapplication.R
import com.ankitkumar.securechatapplication.databinding.SingleChatListItemBinding
import com.ankitkumar.securechatapplication.model.User
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions

class ChatListAdapter(options: FirestoreRecyclerOptions<User>, val onItemClick: OnItemClickListener) : FirestoreRecyclerAdapter<User, ChatListAdapter.ChatListViewHolder>(
    options
) {

    inner class ChatListViewHolder(val bindings : SingleChatListItemBinding) : RecyclerView.ViewHolder(bindings.root){

        fun bind(user: User) {
            bindings.apply {
                //TODO: Add last message support if required
                nameTextView.text = user.name
                phoneNumberTextView.text = user.phoneNo
                displayPictureImageView.setImageResource(R.drawable.icon_display_picture)
                root.setOnClickListener {
                    onItemClick.onClick(user)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatListViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.single_chat_list_item,parent,false)
        return ChatListViewHolder(SingleChatListItemBinding.bind(view))
    }

    override fun onBindViewHolder(holder: ChatListViewHolder, position: Int, user: User) {
        holder.bind(user)
    }

    interface OnItemClickListener {
        fun onClick(user: User)
    }
}
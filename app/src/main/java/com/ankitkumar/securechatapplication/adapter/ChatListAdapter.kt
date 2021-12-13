package com.ankitkumar.securechatapplication.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ankitkumar.securechatapplication.R
import com.ankitkumar.securechatapplication.model.User
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions

class ChatListAdapter(options: FirestoreRecyclerOptions<User>) : FirestoreRecyclerAdapter<User, ChatListAdapter.ChatListViewHolder>(
    options
) {
    var onItemClick: ((User) -> Unit)? = null

    inner class ChatListViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        val displayPicture = itemView.findViewById<ImageView>(R.id.displayPictureImageView)
        val username = itemView.findViewById<TextView>(R.id.nameTextView)
        val phoneNumber = itemView.findViewById<TextView>(R.id.phoneNumberTextView)
        val lastMessage = itemView.findViewById<TextView>(R.id.lastMessageTextView)

        init {
            itemView.setOnClickListener {
                onItemClick?.invoke(getItem(adapterPosition))
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatListViewHolder {
        return ChatListViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.single_chat_list_item,parent,false))
    }

    override fun onBindViewHolder(holder: ChatListViewHolder, position: Int, user: User) {
        holder.displayPicture.setImageResource(R.drawable.icon_display_picture)
        holder.username.text = user.name
        holder.phoneNumber.text = "+91 ${user.phoneNo}"        //no last message support for now
    }
}
package com.ankitkumar.securechatapplication.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ankitkumar.securechatapplication.R
import com.ankitkumar.securechatapplication.databinding.SingleUserItemBinding
import com.ankitkumar.securechatapplication.model.User

class GroupDetailListAdapter(val users: List<User>): RecyclerView.Adapter<GroupDetailListAdapter.UserViewHolder>() {

    inner class UserViewHolder(val bindings: SingleUserItemBinding): RecyclerView.ViewHolder(bindings.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val bindings = SingleUserItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return UserViewHolder(bindings)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        holder.bindings.apply {
            nameTextView.text = users[position].name
            displayPictureImageView.setImageResource(R.drawable.icon_display_picture)
        }
    }

    override fun getItemCount(): Int {
        return users.size
    }
}
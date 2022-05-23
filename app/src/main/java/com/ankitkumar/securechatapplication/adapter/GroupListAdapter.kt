package com.ankitkumar.securechatapplication.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ankitkumar.securechatapplication.R
import com.ankitkumar.securechatapplication.databinding.SingleChatListItemBinding
import com.ankitkumar.securechatapplication.model.Group
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions

class GroupListAdapter(options: FirestoreRecyclerOptions<Group>, val onItemClick: OnItemClickListener) : FirestoreRecyclerAdapter<Group, GroupListAdapter.GroupListViewHolder>(
    options
) {

    inner class GroupListViewHolder(val bindings : SingleChatListItemBinding) : RecyclerView.ViewHolder(bindings.root){

        fun bind(group: Group) {
            bindings.apply {
                nameTextView.text = group.name
                phoneNumberTextView.text = "${group.members.size} members"
                displayPictureImageView.setImageResource(R.drawable.ic_group)
                root.setOnClickListener {
                    onItemClick.onClick(group)
                }
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): GroupListAdapter.GroupListViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.single_chat_list_item,parent,false)
        return GroupListViewHolder(SingleChatListItemBinding.bind(view))
    }

    override fun onBindViewHolder(holder: GroupListAdapter.GroupListViewHolder, p1: Int, group: Group) {
        holder.bind(group)
    }

    interface OnItemClickListener {
        fun onClick(group: Group)
    }
}
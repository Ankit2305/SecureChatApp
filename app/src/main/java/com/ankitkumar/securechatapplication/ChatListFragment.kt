package com.ankitkumar.securechatapplication

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.ankitkumar.securechatapplication.databinding.FragmentChatListBinding

class ChatListFragment: Fragment(R.layout.fragment_chat_list) {

    lateinit var bindings: FragmentChatListBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindings = FragmentChatListBinding.bind(view)
    }
}
package com.ankitkumar.securechatapplication

import android.os.Bundle
import android.os.Message
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.ankitkumar.securechatapplication.databinding.FragmentChatBinding
import com.ankitkumar.securechatapplication.network.WebSocketHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener

class ChatFragment: Fragment(R.layout.fragment_chat) {

    lateinit var bindings: FragmentChatBinding
    lateinit var webSocket: WebSocketHelper

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindings = FragmentChatBinding.bind(view)
        webSocket = WebSocketHelper()

        lifecycleScope.launch {
            webSocket.connect(object : WebSocketListener() {
                override fun onOpen(webSocket: WebSocket, response: Response) {
                    showToast("Connected...")
                }

                override fun onMessage(webSocket: WebSocket, text: String) {
                    showToast(text)
                }

                override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
                    showToast("FAILED...")
                    t.printStackTrace()
                }
            })
        }

    }

    fun showToast(message: String) {
        lifecycleScope.launch(Dispatchers.Main) {
            Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
        }
    }

}
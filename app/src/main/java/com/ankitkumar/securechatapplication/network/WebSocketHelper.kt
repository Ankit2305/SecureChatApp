package com.ankitkumar.securechatapplication.network

import com.ankitkumar.securechatapplication.util.WEB_SOCKET_URL
import com.ankitkumar.securechatapplication.util.WebSocketConnection
import okhttp3.*
import okio.ByteString

class WebSocketHelper {
    private lateinit var webSocket: WebSocket
    private val client: OkHttpClient = OkHttpClient()

    fun connect(webSocketListener: WebSocketListener) {
        val request = Request.Builder().url(WEB_SOCKET_URL).build()
        webSocket = client.newWebSocket(request, webSocketListener)
    }

    fun send(message: String) {
        webSocket.send(message)
    }

    fun close() {
        webSocket.close(123, "Closing connection...")
    }
}
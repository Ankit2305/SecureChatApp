package com.ankitkumar.securechatapplication.network

import android.content.Context
import com.ankitkumar.securechatapplication.util.WEB_SOCKET_URL
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import okhttp3.*

class WebSocketHelper(val webSocketListener: WebSocketListener, val context: Context) {
    private lateinit var webSocket: WebSocket
    private val client: OkHttpClient = OkHttpClient()
    private var isConnected = false
    private var connectionInProgress = false
    val coroutineScope = CoroutineScope(Dispatchers.IO)

    fun connect() {
        if(isConnected || connectionInProgress) {
            return
        }
        connectionInProgress = true
        val request = Request.Builder().url(WEB_SOCKET_URL).build()
        webSocket = client.newWebSocket(request,object: WebSocketListener() {
            override fun onOpen(webSocket: WebSocket, response: Response) {
                webSocketListener.onOpen(webSocket, response)
                isConnected = true
                connectionInProgress = false
            }

            override fun onMessage(webSocket: WebSocket, text: String) {
                webSocketListener.onMessage(webSocket, text)
            }

            override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
                webSocketListener.onFailure(webSocket, t, response)
                isConnected = false
                connectionInProgress = false
            }
        })
        //connectionService()
    }

    fun send(message: String) {
        webSocket.send(message)
    }

    fun close() {
        webSocket.close(123, "Closing connection...")
    }

    fun connectionService() {
        coroutineScope.launch {
            while(true) {
                delay(5000)
                if(!isConnected) {
                    connect()
                }
            }
        }
    }
}
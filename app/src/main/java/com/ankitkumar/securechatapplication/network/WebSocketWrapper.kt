package com.ankitkumar.securechatapplication.network

import android.content.Context
import android.provider.Settings
import android.util.Log
import com.ankitkumar.securechatapplication.R
import com.ankitkumar.securechatapplication.SecureChatApplication
import com.ankitkumar.securechatapplication.crypto.AES
import com.ankitkumar.securechatapplication.keyExchangeProtocol.DiffieHellmanKeyExchange
import com.ankitkumar.securechatapplication.model.Auth
import com.ankitkumar.securechatapplication.model.Message
import com.ankitkumar.securechatapplication.repository.ChatRepository
import com.ankitkumar.securechatapplication.util.AUTH_CODE
import com.ankitkumar.securechatapplication.util.MessageType
import com.ankitkumar.securechatapplication.util.PreferenceHelper
import com.ankitkumar.securechatapplication.util.WEB_SOCKET_URL
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import okhttp3.*
import java.math.BigInteger
import java.security.Key
import java.security.MessageDigest
import java.security.SecureRandom
import javax.crypto.KeyGenerator

class WebSocketWrapper(val repository: ChatRepository) {
    private lateinit var webSocket: WebSocket
    private val client: OkHttpClient = OkHttpClient()
    private var isConnected = false
    private var connectionInProgress = false
    val webSocketScope = CoroutineScope(Dispatchers.IO)

    fun connect() {
        if(isConnected || connectionInProgress) {
            return
        }
        connectionInProgress = true
        val request = Request.Builder().url(WEB_SOCKET_URL).build()
        webSocket = client.newWebSocket(request,object: WebSocketListener() {
            override fun onOpen(webSocket: WebSocket, response: Response) {
                isConnected = true
                connectionInProgress = false
                checkForReceivedMessages()
                performKeyExchange()
            }

            override fun onMessage(webSocket: WebSocket, text: String) {
                handleMessage(text)
            }

            override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
                isConnected = false
                connectionInProgress = false
            }
        })
        //connectionService()
    }

    private fun checkForReceivedMessages() {
        TODO("check for messages received while USEr is offline")

    }

    private fun performKeyExchange() {
        if(isSessionAlreadyBuilt){
            return
        }else{
            val receiverKeyBundle : List<Long> = getKeyBundleFromServer()
            val identityKey = PreferenceHelper.getIdentityKey(SecureChatApplication.applicationContext())
            val signedPreKey = PreferenceHelper.getSignedPreKey(SecureChatApplication.applicationContext())
            val curveParams = PreferenceHelper.getCurveParams(SecureChatApplication.applicationContext())

            val dh1 = DiffieHellmanKeyExchange.computeCommonSecretKey(identityKey, receiverKeyBundle[1], curveParams.second)
            val dh2 = DiffieHellmanKeyExchange.computeCommonSecretKey(signedPreKey, receiverKeyBundle[0], curveParams.second)
            val dh3 = DiffieHellmanKeyExchange.computeCommonSecretKey(signedPreKey, receiverKeyBundle[1], curveParams.second)

            //after X3DH we'll compute common secret key to encrypt and decrypt message
            val concatenate = dh1.toString() + dh2.toString() + dh3.toString()
            val key = concatenate.hashCode()

            //share keys with receiver
            val identityKeyPublic = PreferenceHelper.getIdentityKeyPublic(SecureChatApplication.applicationContext())
            val signedPreKeyPublic = PreferenceHelper.getSignedPreKeyPublic(SecureChatApplication.applicationContext())
            val keyList = listOf(identityKeyPublic, signedPreKeyPublic, receiverKeyBundle[1])
            shareKeyBundleWithReceiver()
        }
    }

    private fun getKeyBundleFromServer(): List<Long> {
        TODO("Not yet implemented")
    }

    private fun shareKeyBundleWithReceiver() {
        TODO("Not yet implemented")
    }


    fun authorize(context: Context) {
        val authCode = PreferenceHelper.getAuthCode(context)

        if(authCode.isNotEmpty()) {
            val authJson = Auth(
                MessageType.AUTH,
                authCode
            )
            val json = GsonHelper.getJson(authJson)
            Log.d("ChatFragment",json)
            send(json)
        }

    }

    private fun handleMessage(json: String) {
        try {
            val encryptedMessage = GsonHelper.getEncryptedMessageFromJson(json)
            val message = encryptedMessage?.toMessage()
            webSocketScope.launch {
                message?.let {
                    repository.insertMessage(message)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun send(message: Message) {
        webSocketScope.launch {
            Log.i("WebSocketTag", "send: $message")
            val encryptedMessage = message.encryptMessage()
            val json = GsonHelper.getJson(encryptedMessage)
            send(json)
            message.setAsSendMessage()
            repository.insertMessage(message)
        }
    }

    fun send(json: String) {
        Log.i("WebSocketTag", "send: $json")
        webSocket.send(json)
    }

    private fun encrypt(json: String): String {
        val encryptedJson = AES.encrypt(json) ?: ""
        Log.i("WebSocketTag", "encrypt: $encryptedJson")
        return encryptedJson
    }
    private fun decrypt(encryptedJson: String): String {
        val json = AES.decrypt(encryptedJson) ?: ""
        Log.i("WebSocketTag", "encrypt: $json")
        return json
    }

    fun close() {
        webSocket.close(123, "Closing connection...")
    }

    fun connectionService() {
        webSocketScope.launch {
            while(true) {
                delay(5000)
                if(!isConnected) {
                    connect()
                }
            }
        }
    }
}
package com.ankitkumar.securechatapplication.util

const val WEB_SOCKET_URL = "wss://secure-chat-application.herokuapp.com/"
//const val WEB_SOCKET_URL = "ws://192.168.2.171:3000/"
const val AUTH_CODE = "authCode"

//TODO Use key sharing
const val KEY = "SomeKey"
//
//object WebSocketConnection {
//    const val NEW_CONNECTION_REQUEST = 1000
//}

object MessageType{
    const val MESSAGE = 2000
    const val AUTH = 1000
}
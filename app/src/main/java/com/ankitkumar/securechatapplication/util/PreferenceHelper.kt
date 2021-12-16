package com.ankitkumar.securechatapplication.util

import android.content.Context
import com.ankitkumar.securechatapplication.R

object PreferenceHelper {

    fun getAuthCode(context: Context): String {
        val sharedPreferences = context.getSharedPreferences(context.getString(R.string.app_name),Context.MODE_PRIVATE)
        val authCode = sharedPreferences?.getString(AUTH_CODE,"12345")
        return authCode ?: ""
    }

    fun getUserId(context: Context): String = getAuthCode(context)
}
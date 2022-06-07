package com.ankitkumar.securechatapplication.util

import android.content.Context
import com.ankitkumar.securechatapplication.R

object PreferenceHelper {

    fun getAuthCode(context: Context): String {
        val sharedPreferences = context.getSharedPreferences(context.getString(R.string.app_name),Context.MODE_PRIVATE)
        val authCode = sharedPreferences?.getString(AUTH_CODE,"12345")
        return authCode ?: ""
    }

    fun getUserName(context: Context): String {
        val sharedPreferences = context.getSharedPreferences(context.getString(R.string.app_name),Context.MODE_PRIVATE)
        val userId = sharedPreferences?.getString(USER_NAME, "")
        return userId ?: ""
    }

    fun setUserName(context: Context, userName: String) {
        val sharedPreferences = context.getSharedPreferences(context.getString(R.string.app_name),Context.MODE_PRIVATE)
        with(sharedPreferences.edit()) {
            putString(USER_NAME,userName)
            apply()
        }
    }

    fun setAuthCode(context: Context, authCode: String) {
        val sharedPreferences = context.getSharedPreferences(context.getString(R.string.app_name),Context.MODE_PRIVATE)
        with(sharedPreferences.edit()) {
            putString(AUTH_CODE, authCode)
            apply()
        }
    }

}
package com.ankitkumar.securechatapplication.util

import android.content.Context
import com.ankitkumar.securechatapplication.R
import org.koin.dsl.module.applicationContext

object PreferenceHelper {

    fun getAuthCode(context: Context): String {
        val sharedPreferences = context.getSharedPreferences(context.getString(R.string.app_name),Context.MODE_PRIVATE)
        val authCode = sharedPreferences?.getString(AUTH_CODE,"12345")
        return authCode ?: ""
    }

    fun getUserId(context: Context): String = getAuthCode(context)

    fun getIdentityKey(context: Context):Long{
        val sharedPreferences = context.getSharedPreferences(context.getString(R.string.app_name),Context.MODE_PRIVATE)
        val identityKey  = sharedPreferences?.getLong(IDENTITY_KEY, 0)
        return identityKey ?: 0
    }

    fun getIdentityKeyPublic(context: Context):Long{
        val sharedPreferences = context.getSharedPreferences(context.getString(R.string.app_name),Context.MODE_PRIVATE)
        val identityKey  = sharedPreferences?.getLong(IDENTITY_KEY_PUBLIC, 0)
        return identityKey ?: 0
    }

    fun getSignedPreKey(context: Context):Long{
        val sharedPreferences = context.getSharedPreferences(context.getString(R.string.app_name),Context.MODE_PRIVATE)
        val identityKey  = sharedPreferences?.getLong(SIGNED_PRE_KEY, 0)
        return identityKey ?: 0
    }

    fun getSignedPreKeyPublic(context: Context):Long{
        val sharedPreferences = context.getSharedPreferences(context.getString(R.string.app_name),Context.MODE_PRIVATE)
        val identityKey  = sharedPreferences?.getLong(SIGNED_PRE_KEY_PUBLIC, 0)
        return identityKey ?: 0
    }

    fun saveCurveParams(curveParams: Pair<Int, Int>, context: Context) {
        val sharedPreferences = context.getSharedPreferences(context.getString(R.string.app_name),Context.MODE_PRIVATE)
        with (sharedPreferences.edit()) {
            putInt(CURVE_G,curveParams.first)
            putInt(IDENTITY_KEY, curveParams.second)
            apply()
        }
    }

    fun getCurveParams(context: Context): Pair<Int, Int> {
        val sharedPreferences = context.getSharedPreferences(context.getString(R.string.app_name),Context.MODE_PRIVATE)
        val curve_g  = sharedPreferences?.getInt(CURVE_G, 0) ?: 0
        val curve_n  = sharedPreferences?.getInt(CURVE_N, 0) ?: 0
        return Pair(curve_g, curve_n)
    }

    fun saveKeyBundle(keyBundles: List<Pair<Long, Long>>, context: Context) {
        val sharedPreferences = context.getSharedPreferences(context.getString(R.string.app_name),Context.MODE_PRIVATE)
        with (sharedPreferences.edit()) {
            putLong(IDENTITY_KEY_PUBLIC,keyBundles[0].second)
            putLong(SIGNED_PRE_KEY, keyBundles[1].first)
            putLong(SIGNED_PRE_KEY_PUBLIC, keyBundles[1].second)
            apply()
        }
    }
}
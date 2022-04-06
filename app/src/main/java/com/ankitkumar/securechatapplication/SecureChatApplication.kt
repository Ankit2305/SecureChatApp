package com.ankitkumar.securechatapplication

import android.app.Application
import android.content.Context
import com.ankitkumar.securechatapplication.di.appModule
import org.koin.android.ext.android.startKoin

class SecureChatApplication : Application() {
    init {
        instance = this
    }

    companion object{
        private var instance: SecureChatApplication? = null

        fun applicationContext(): Context {
            return instance!!.applicationContext
        }
    }
    override fun onCreate() {
        super.onCreate()
        startKoin(this, listOf(appModule))
        val context : Context = SecureChatApplication.applicationContext()
    }
}
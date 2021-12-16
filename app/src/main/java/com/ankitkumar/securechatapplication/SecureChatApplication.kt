package com.ankitkumar.securechatapplication

import android.app.Application
import com.ankitkumar.securechatapplication.di.appModule
import org.koin.android.ext.android.startKoin

class SecureChatApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin(this, listOf(appModule))
    }
}
package com.ankitkumar.securechatapplication.di

import android.content.Context
import androidx.room.Room
import com.ankitkumar.securechatapplication.db.ChatDatabase
import com.ankitkumar.securechatapplication.network.WebSocketHelper
import com.ankitkumar.securechatapplication.network.WebSocketWrapper
import com.ankitkumar.securechatapplication.repository.ChatRepository
import com.ankitkumar.securechatapplication.viewmodel.ChatViewModel
//import com.ankitkumar.securechatapplication.viewmodel.ChatViewModel
import org.koin.android.viewmodel.ext.koin.viewModel
import org.koin.dsl.module.module

val appModule = module {
    viewModel { ChatViewModel(get(), get()) }
    single { getChatDatabase(get()) }
    single { ChatRepository(get()) }
    single { WebSocketWrapper(get()) }
}

fun getChatDatabase(context: Context): ChatDatabase = Room.databaseBuilder(
    context,
    ChatDatabase::class.java,
    "chat_database"
)
    .fallbackToDestructiveMigration()
    .build()
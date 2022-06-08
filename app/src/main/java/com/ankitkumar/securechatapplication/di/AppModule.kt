package com.ankitkumar.securechatapplication.di

//import com.ankitkumar.securechatapplication.viewmodel.ChatViewModel
import android.content.Context
import androidx.room.Room
import com.ankitkumar.securechatapplication.db.ChatDatabase
import com.ankitkumar.securechatapplication.network.ChatApiService
import com.ankitkumar.securechatapplication.network.WebSocketWrapper
import com.ankitkumar.securechatapplication.repository.ChatRepository
import com.ankitkumar.securechatapplication.util.API_URL
import com.ankitkumar.securechatapplication.viewmodel.ChatViewModel
import com.google.gson.GsonBuilder
import org.koin.android.viewmodel.ext.koin.viewModel
import org.koin.dsl.module.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


val appModule = module {
    viewModel { ChatViewModel(get(), get()) }
    single { getChatDatabase(get()) }
    single { ChatRepository(get(), get()) }
    single { WebSocketWrapper(get()) }
    single { getChatApiService(get()) }
}

fun getChatDatabase(context: Context): ChatDatabase = Room.databaseBuilder(
    context,
    ChatDatabase::class.java,
    "chat_database"
)
    .fallbackToDestructiveMigration()
    .build()

fun getChatApiService(context: Context): ChatApiService {
    val retrofit = Retrofit.Builder()
        .baseUrl(API_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    return retrofit.create(ChatApiService::class.java)
}
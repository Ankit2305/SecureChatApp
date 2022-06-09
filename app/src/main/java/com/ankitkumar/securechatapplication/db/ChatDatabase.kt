package com.ankitkumar.securechatapplication.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.ankitkumar.securechatapplication.model.Message

@Database(entities = [Message::class], version = 4)
abstract class ChatDatabase : RoomDatabase() {
    abstract fun messageDao(): MessageDao
}
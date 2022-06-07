package com.ankitkumar.securechatapplication.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.ankitkumar.securechatapplication.model.Message

@Dao
interface MessageDao {

    @Insert
    fun insertMessage(message: Message)

    @Delete
    fun deleteMessage(message: Message)

    @Query("SELECT * FROM message WHERE (sender = :userId OR `to` = :userId) AND isGroupMessage = :isGroupChat ORDER BY timeStamp DESC LIMIT 1000")
    fun getMessages(userId: String, isGroupChat: Boolean): LiveData<List<Message>>

    @Query("SELECT COUNT(*) FROM message WHERE sender = :userId OR `to` = :userId")
    fun getMessageCount(userId: String): LiveData<Int>

    @Query("SELECT COUNT(*) FROM message WHERE id = :messageId")
    fun containsMessage(messageId: String): Int
}
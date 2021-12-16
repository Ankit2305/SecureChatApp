package com.ankitkumar.securechatapplication.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.ankitkumar.securechatapplication.model.Message

@Dao
interface MessageDao {

    @Insert
    fun insertMessage(message: Message)

    @Delete
    fun deleteMessage(message: Message)

    @Query("SELECT * FROM message WHERE sender = :userId OR `to` = :userId ORDER BY timeStamp DESC LIMIT 1000")
    fun getMessages(userId: String): LiveData<List<Message>>

    @Query("SELECT COUNT(*) FROM message WHERE sender = :userId OR `to` = :userId")
    fun getMessageCount(userId: String)

}
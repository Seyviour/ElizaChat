package com.example.elizachat.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.elizachat.Message

@Dao
interface ConversationDao{

    @Insert
    fun addMessage(message: Message)

    @Query("SELECT * FROM message")
    fun getMessages(): LiveData<List<Message>>

}


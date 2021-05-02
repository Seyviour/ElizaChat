package com.example.elizachat.database

import androidx.room.Dao
import androidx.room.Query
import com.example.elizachat.Message

@Dao
interface CrimeDao{

    @Query("SELECT * FROM message")
    fun getMessages(): List<Message>
}
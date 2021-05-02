package com.example.elizachat.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.elizachat.Message


@Database(entities = [ Message::class ], version = 1)
@TypeConverters(MessageTypeConverters::class)
abstract class ConversationDatabase: RoomDatabase() {
}
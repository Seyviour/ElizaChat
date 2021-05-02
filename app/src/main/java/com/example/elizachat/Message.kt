package com.example.elizachat

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity
data class Message (
    @PrimaryKey val id: UUID = UUID.randomUUID(),
    val user:String,
    val message: String,
    val time: Long,
    val sent: Boolean = false
){
}
package com.example.elizachat

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.room.Room
import com.example.elizachat.database.ConversationDao
import com.example.elizachat.database.ConversationDatabase
import java.lang.IllegalStateException
import java.util.concurrent.Executors

private const val DATABASE_NAME = "conversation-database"
class ConversationRepository private constructor(context: Context){

    //INITIALIZATION
    private val database: ConversationDatabase = Room.databaseBuilder(
        context.applicationContext,
        ConversationDatabase::class.java,
        DATABASE_NAME
    ).build()

    private val conversationDao = database.conversationDao()

    val allMessages : LiveData<List<Message>>?

    init{
        allMessages = conversationDao.getMessages()
    }

    companion object {
        private var INSTANCE: ConversationRepository? = null
        fun initialize(context: Context) {
            if (INSTANCE == null){
                INSTANCE = ConversationRepository(context)
            }
        }
        fun get(): ConversationRepository{
            return INSTANCE?: throw IllegalStateException("Conversation Repository must be initialized")
        }
    }

    //DATA RETRIEVAL

    val messageList = MutableLiveData<List<Message>>()

    fun getMessages(): LiveData<List<Message>> = conversationDao.getMessages()

    //DATA UPDATE AND INSERTION
    private val executor = Executors.newSingleThreadExecutor()

    fun addMessage(message: Message){
        executor.execute {conversationDao.addMessage(message)}
    }

}
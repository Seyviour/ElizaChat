package com.example.elizachat

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.elizachat.eliza.ElizaAndroid
import com.example.elizachat.eliza.Scripts
import java.util.*

class ChatViewModel : ViewModel() {

    private val conversationRepository = ConversationRepository.get()

    private val elizaBot = ElizaAndroid(Scripts.doctor)

    fun elizaGenerateReply(string: String): List<Message>{
        val reply = elizaBot.process(string)
        return reply.map { Message(
                user="Eliza",
                message=it,
                sent = false,
                time = Calendar.getInstance().timeInMillis
        ) }
    }

    fun elizaHasMemory(): Boolean{
        return elizaBot.memory.isEmpty()
    }

    private val messages = mutableListOf<Message>()
    var messagesLiveData: LiveData<List<Message>> = conversationRepository.getMessages()

    /**
    init{
        for (i in 0 until 100){
            val sent = i.rem(2) == 0
            val user = if (sent) "Saviour" else "Eliza"
            val message = Message(
                user = user,
                message = "this is a message",
                time = System.currentTimeMillis(),
                sent = sent
            )
            addMessage(message)
        }
    }
    **/
    fun addMessage(message: Message){

        //messagesLiveData.value = messages
    }

    fun saveMessage(message: Message){
        conversationRepository.addMessage(message)
    }

    fun getItemCount() = messages.size
}
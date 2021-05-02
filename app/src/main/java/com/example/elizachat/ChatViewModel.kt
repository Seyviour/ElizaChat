package com.example.elizachat

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ChatViewModel : ViewModel() {
    private val messages = mutableListOf<Message>()
    var messagesLiveData: MutableLiveData<List<Message>> = MutableLiveData()

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

    fun addMessage(message: Message){
        messages.add(message)
        messagesLiveData.value = messages
    }

    fun getItemCount() = messages.size
}
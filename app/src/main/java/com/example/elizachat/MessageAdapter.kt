package com.example.elizachat

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.received_message.view.*
import kotlinx.android.synthetic.main.sent_message.view.*
import java.text.SimpleDateFormat
import java.util.*

private const val VIEW_TYPE_SENT_MESSAGE = 1
private const val VIEW_TYPE_RECEIVED_MESSAGE = 2

class MessageAdapter(): RecyclerView.Adapter<MessageViewHolder>(){

    private var messages: List<Message>? = null

    fun setMessageList(messageList: MutableList<Message>){
        messages = messageList
        notifyDataSetChanged()
    }

    object DateUtils {
        fun fromMillisToTimeString(millis: Long): String {
            val format = SimpleDateFormat("hh:mm a", Locale.getDefault())
            return format.format(millis)
        }
    }

    inner class SentMessageViewHolder (view: View): MessageViewHolder(view){
        private var messageText: TextView = view.txtSentMessage
        private var timeText: TextView = view.txtTimeSent

        override fun bind(message: Message){
            messageText.text = message.message
            timeText.text = DateUtils.fromMillisToTimeString(message.time)
        }
    }

    inner class ReceivedMessageViewHolder (view: View): MessageViewHolder(view){
        private var messageText: TextView = view.txtReceivedMessage
        private var timeText: TextView = view.txtTimeReceived

        override fun bind(message: Message){
            messageText.text = message.message
            timeText.text = DateUtils.fromMillisToTimeString(message.time)
        }
    }

    override fun getItemCount(): Int {
        return  messages!!.size
    }

    override fun getItemViewType(position: Int): Int {
        val message = messages!![position]
        return if (message.sent) VIEW_TYPE_SENT_MESSAGE else VIEW_TYPE_RECEIVED_MESSAGE
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        return if(viewType == VIEW_TYPE_SENT_MESSAGE){
            SentMessageViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.sent_message, parent, false))
        } else {
            ReceivedMessageViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.received_message, parent, false))
        }
    }

    override fun onBindViewHolder(holder: MessageViewHolder, position: Int){
        val item = holder.itemView
        messages.let{
            val message = it!![position]
            holder?.bind(message)
        }

    }


}

open class MessageViewHolder(view: View): RecyclerView.ViewHolder(view) {

    open fun bind(message: Message){

    }
}
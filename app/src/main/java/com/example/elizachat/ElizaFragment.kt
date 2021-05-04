package com.example.elizachat

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.get
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.fragment_eliza.*
import java.util.*
import androidx.lifecycle.Observer
import androidx.lifecycle.observe
import kotlinx.android.synthetic.main.card_message.view.*

private const val TAG = "ElizaFragment"

class ElizaFragment : Fragment() {

    private val chatViewModel: ChatViewModel by lazy{
        ViewModelProvider(this).get(ChatViewModel::class.java)
    }
    private lateinit var chatRecyclerView: RecyclerView
    private var adapter: MessageAdapter? = MessageAdapter().apply { setMessageList(emptyList()) }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //Log.d(TAG, "Total Messages: ${chatViewModel.messages.size}")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_eliza, container, false)

        //RECYCLER VIEW SETUP
        chatRecyclerView = view.findViewById(R.id.chatRecyclerView) as RecyclerView
        chatRecyclerView.layoutManager = LinearLayoutManager(context).apply {
            stackFromEnd = true
        }
        chatRecyclerView.adapter = adapter

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

       // var scond: Boolean = false
        //val cond = chatViewModel.messagesLiveData.value?.size
        chatViewModel.messagesLiveData.observe(viewLifecycleOwner, messageObserver)
        /**
        if (chatViewModel.elizaHasMemory() and !scond){
            val elizaPrompt = Message(
                    user="Eliza",
                    message ="What's on your mind",
                    sent=false,
                    time=Calendar.getInstance().timeInMillis
            )
            onMessageSend(elizaPrompt)
         }
**/

    }

    override fun onStart() {
        super.onStart()



        //SEND BUTTON LISTENER
        sendButton.setOnClickListener {


            if (messageText.text.isNotEmpty()){

                messageText.hint = "Enter Message"
                val message = Message(
                        user ="user",



                        message = messageText.text.toString(),
                        time = Calendar.getInstance().timeInMillis,
                        sent = true
                )
                val messageText = messageText.text.toString()
                onMessageSend(message)
                resetInput()
                elizaReply(messageText)
            }
        }
    }
 /**
    private fun updateUI(){
        adapter = MessageAdapter()
        chatRecyclerView.adapter = adapter
    }
**/

    private fun updateUI(messageList: List<Message>){
     adapter!!.setMessageList(messageList)
     chatRecyclerView.layoutManager = LinearLayoutManager(context).apply { stackFromEnd = true }
     }

    companion object {
        @JvmStatic
        fun newInstance() = ElizaFragment()
    }

    private fun onMessageSend(message: Message){
        chatViewModel.saveMessage(message)
        chatRecyclerView.smoothScrollToPosition(chatViewModel.messagesLiveData.value!!.size);
    }

    private fun elizaReply(string: String){
        val elizaMessages = chatViewModel.elizaGenerateReply(string)
        for (elizaMessage in elizaMessages) {
            onMessageSend(elizaMessage)
        }

    }


    private val messageObserver = Observer<List<Message>> { newMessageList ->
        updateUI(newMessageList)
    }


    private fun resetInput(){
       // val inputManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        messageText.text.clear()
        //inputManager.hideSoftInputFromWindow(currentFocus!!.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
    }
}
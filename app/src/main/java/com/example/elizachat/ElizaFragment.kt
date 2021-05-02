package com.example.elizachat

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.fragment_eliza.*
import java.util.*
import androidx.lifecycle.Observer
import androidx.lifecycle.observe

private const val TAG = "ElizaFragment"

class ElizaFragment : Fragment() {

    private val chatViewModel: ChatViewModel by lazy{
        ViewModelProvider(this).get(ChatViewModel::class.java)
    }
    private lateinit var chatRecyclerView: RecyclerView
    private var adapter: MessageAdapter? = null
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
        chatRecyclerView = view.findViewById(R.id.chatRecyclerView) as RecyclerView
        val lm = LinearLayoutManager(context)
        lm.stackFromEnd = true
        chatRecyclerView.layoutManager = lm
        updateUI()
        chatViewModel.messagesLiveData.observe(this, messageObserver)
        return view
    }

    override fun onStart() {
        super.onStart()
        sendButton.setOnClickListener {
            if (messageText.text.isNotEmpty()){
                val message = Message(
                        "user",
                        messageText.text.toString(),
                        Calendar.getInstance().timeInMillis,
                        sent = true
                )
                onMessageSend(message)
                resetInput()
            }
        }
    }

    private fun updateUI(){
        adapter = MessageAdapter()
        chatRecyclerView.adapter = adapter
    }

    companion object {
        @JvmStatic
        fun newInstance() = ElizaFragment()
    }

    fun onMessageSend(message: Message){
       chatViewModel.addMessage(message)
        chatRecyclerView.smoothScrollToPosition(chatViewModel.getItemCount());
    }


    private val messageObserver = Observer<List<Message>> { newMessageList ->
        adapter?.setMessageList(newMessageList as MutableList<Message>)
    }


    private fun resetInput(){
       // val inputManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        messageText.text.clear()
        //inputManager.hideSoftInputFromWindow(currentFocus!!.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
    }


}
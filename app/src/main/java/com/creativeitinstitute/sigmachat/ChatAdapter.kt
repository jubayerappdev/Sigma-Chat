package com.creativeitinstitute.sigmachat

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.creativeitinstitute.sigmachat.data.models.TextMessages

class ChatAdapter(var userIDSelf: String, private val chatList : MutableList<TextMessages>) : RecyclerView.Adapter<ChatAdapter.ChatViewHolder>() {

    private val LEFT : Int = 1
    private val RIGHT: Int = 2




    class ChatViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){

        var messageTV:TextView = itemView.findViewById(R.id.chatTV)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder {

        return if (viewType==RIGHT){
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_right_chat, parent, false)
            ChatViewHolder(view)
        }else{
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_left_chat, parent, false)
            ChatViewHolder(view)
        }

    }

    override fun onBindViewHolder(holder: ChatViewHolder, position: Int) {

    val message = chatList[position]
        holder.messageTV.text = message.text

    }

    override fun getItemViewType(position: Int): Int {
        return if (chatList.get(position).senderID==userIDSelf){
            RIGHT
        }else{
            LEFT
        }
    }

    override fun getItemCount(): Int {
        return chatList.size
    }


}
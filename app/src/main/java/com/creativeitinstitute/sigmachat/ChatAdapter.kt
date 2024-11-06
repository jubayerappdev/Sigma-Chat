package com.creativeitinstitute.sigmachat

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.creativeitinstitute.sigmachat.data.models.TextMessages

class ChatAdapter(var userIDSelf: String) : ListAdapter<TextMessages, ChatAdapter.ChatViewHolder>(COMPARATOR) {

    val LEFT : Int = 1
    val RIGHT: Int = 2

    val chatList = mutableListOf<TextMessages>()


    class ChatViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){

        var messageTV:TextView = itemView.findViewById(R.id.chatTV)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder {

        if (viewType==RIGHT){
            var view = LayoutInflater.from(parent.context).inflate(R.layout.item_right_chat, parent, false)
            return ChatViewHolder(view)
        }else{
            var view = LayoutInflater.from(parent.context).inflate(R.layout.item_left_chat, parent, false)
            return ChatViewHolder(view)
        }

    }

    override fun onBindViewHolder(holder: ChatViewHolder, position: Int) {

        getItem(position).apply {
            chatList.add(this)
            holder.messageTV.text = this.text
        }

    }

    override fun getItemViewType(position: Int): Int {
        return if (chatList.get(position).senderID==userIDSelf){
            RIGHT
        }else{
            LEFT
        }
    }

    companion object{
        var COMPARATOR = object : DiffUtil.ItemCallback<TextMessages>() {
            override fun areItemsTheSame(oldItem: TextMessages, newItem: TextMessages): Boolean {

                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: TextMessages, newItem: TextMessages): Boolean {

                return oldItem.msgID == newItem.msgID
            }

        }
    }
}
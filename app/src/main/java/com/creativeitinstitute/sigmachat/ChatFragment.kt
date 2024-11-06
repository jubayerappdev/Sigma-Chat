package com.creativeitinstitute.sigmachat

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import coil.load
import com.bumptech.glide.Glide
import com.creativeitinstitute.sigmachat.data.models.TextMessages
import com.creativeitinstitute.sigmachat.databinding.FragmentChatBinding
import com.creativeitinstitute.sigmachat.nodes.DBNODES
import com.creativeitinstitute.sigmachat.utils.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.util.UUID


class ChatFragment : Fragment() {
    lateinit var binding: FragmentChatBinding
    lateinit var chatDB:DatabaseReference
    lateinit var userIDSelf :String
    lateinit var userIDRemote:String

    val chatList = mutableListOf<TextMessages>()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        binding = FragmentChatBinding.inflate(inflater, container, false)

        requireArguments().getString(USERID)?.let {
            userIDRemote = it
        }
        chatDB = FirebaseDatabase.getInstance().reference

        FirebaseAuth.getInstance().currentUser?.let {
            userIDSelf  = it.uid
        }
        chatDB.child(DBNODES.USER).child(userIDRemote).addValueEventListener(
            object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {

                    snapshot.getValue(User::class.java)?.let {
                        binding.nameTV.text = it.fullName
                        binding.emailTV.text = it.email
                       Glide.with(requireContext()).load(it.profileImage).placeholder(R.drawable.placeholder)
                           .into(binding.profileImage)
                    }


                }

                override fun onCancelled(error: DatabaseError) {

                }
            }
        )

        messageToShow()

        return binding.root
    }

    private fun messageToShow() {
        chatDB.child(DBNODES.CHAT).addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {

                chatList.clear()
                snapshot.children.forEach {snp->
                    snp.getValue(TextMessages::class.java)?.let {

                        if (it.senderID==userIDSelf && it.receiver==userIDRemote
                            || it.senderID == userIDRemote && it.receiver==userIDSelf
                            ){

                            chatList.add(it)
                        }


                    }

                }
                val adapter = ChatAdapter(userIDSelf)
                adapter.submitList(chatList)
                binding.chatRcv.adapter = adapter


            }

            override fun onCancelled(error: DatabaseError) {

            }

        })





    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.sendBtn.setOnClickListener {

            var textMessage = TextMessages(text = binding.inputMsg.text.toString(), senderID = userIDSelf, receiver = userIDRemote, msgID = "")

            sendTextMessage(textMessage)
        }

    }

    private fun sendTextMessage(textMessage: TextMessages) {
        var msgID = chatDB.push().key ?: UUID.randomUUID().toString()
        textMessage.msgID = msgID
        chatDB.child(DBNODES.CHAT).child(msgID).setValue(textMessage).addOnCompleteListener {

            if (it.isSuccessful){
                Toast.makeText(requireContext(),"Message Sent", Toast.LENGTH_SHORT).show()
                binding.inputMsg.setText("")
            }else{
                Toast.makeText(requireContext(), it.exception?.message?: "Something went wrong!", Toast.LENGTH_SHORT).show()
            }
        }

    }

    companion object{
        const val USERID = "user_id_key"
    }


}
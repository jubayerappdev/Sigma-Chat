package com.creativeitinstitute.sigmachat

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import coil.load
import com.creativeitinstitute.sigmachat.databinding.FragmentProfileBinding
import com.creativeitinstitute.sigmachat.nodes.DBNODES
import com.creativeitinstitute.sigmachat.utils.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class ProfileFragment : Fragment() {

    lateinit var binding: FragmentProfileBinding
    lateinit var userDB:DatabaseReference

    private var currentUser:FirebaseUser?=null

    private var userId = ""

    val bundle = Bundle()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentProfileBinding.inflate(inflater, container,false)
        userDB = FirebaseDatabase.getInstance().reference
        requireArguments().getString(USERID)?.let {

            userId = it
            getUserByID(it)
        }

        FirebaseAuth.getInstance().currentUser?.let {
            if (userId==it.uid){
                binding.chatWithUserBtn.text = EDIT
            }else{
                binding.chatWithUserBtn.text = CHAT
            }
            binding.chatWithUserBtn.setOnClickListener {
                bundle.putString(EditProfileFragment.USERID,userId)
                if (binding.chatWithUserBtn.text == EDIT){


                    findNavController().navigate(R.id.action_profileFragment_to_editProfileFragment, bundle)
                }else{
                    findNavController().navigate(R.id.action_profileFragment_to_chatFragment, bundle)
                }
            }
        }



        return binding.root
    }
    companion object{
        const val USERID = "user_id_key"
        const val EDIT = "Edit Profile"
        const val CHAT = "Let's Chat"
    }

    private fun getUserByID(userId: String) {

        userDB.child(DBNODES.USER).child(userId).addValueEventListener(
            object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {

                    snapshot.getValue(User::class.java)?.let {
                        binding.userEmail.text = it.email
                        binding.bio.text = it.bio
                        binding.fullName.text = it.fullName

                        binding.profileImage.load(it.profileImage)
                    }


                }

                override fun onCancelled(error: DatabaseError) {

                }
            }
        )


    }





}
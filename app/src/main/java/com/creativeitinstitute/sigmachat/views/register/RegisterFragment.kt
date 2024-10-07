package com.creativeitinstitute.sigmachat.views.register

import android.os.Bundle
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.creativeitinstitute.sigmachat.R
import com.creativeitinstitute.sigmachat.databinding.FragmentRegisterBinding
import com.creativeitinstitute.sigmachat.nodes.DBNODES
import com.creativeitinstitute.sigmachat.utils.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase


class RegisterFragment : Fragment() {

    lateinit var binding: FragmentRegisterBinding
    lateinit var userDB:DatabaseReference


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        binding = FragmentRegisterBinding.inflate(inflater, container, false)

        userDB = FirebaseDatabase.getInstance().reference

        binding.loginBtn.setOnClickListener {
            findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
        }

        binding.registerBtn.setOnClickListener {
            val name = binding.userName.text.toString().trim()
            val email = binding.userEmail.text.toString().trim()
            val password = binding.userPassword.text.toString().trim()

            if (isValidEmail(email) && isValidPassword(password)){

                registerUser(name,email,password)
            }else{

                Toast.makeText(requireContext(), "Invalid email or password", Toast.LENGTH_LONG).show()
            }

        }



        return binding.root

    }

    private fun registerUser(name: String, email: String, password: String) {

        val jAuth= FirebaseAuth.getInstance()

        jAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener { task->

            if (task.isSuccessful){
                saveUserToDataBase(jAuth.currentUser?.uid, email, name)
            }else{
                Toast.makeText(context, "Registration Failed : ${task.exception?.message}", Toast.LENGTH_LONG).show()
            }

        }

    }

    private fun saveUserToDataBase(uid: String?, email: String, name: String) {

        uid?.let {
            val user = User(
                userId = uid, email = email, fullName = name
            )

            userDB.child(DBNODES.USER).child(it).setValue(user).addOnCompleteListener { task->

                if (task.isSuccessful){

                    findNavController().navigate(R.id.action_registerFragment_to_homeFragment)
                }else{

                    Toast.makeText(requireContext(), "${task.exception?.message}", Toast.LENGTH_LONG).show()
                }

            }

        }

    }


    fun isValidEmail(email: String): Boolean {
        return email.isNotEmpty() && Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    fun isValidPassword(password: String): Boolean {
        // Define the password validation criteria
        val passwordPattern = "^(?=.*[0-9])(?=.*[a-zA-Z]).{8,}$"
        return password.isNotEmpty() && password.matches(Regex(passwordPattern))
    }


}
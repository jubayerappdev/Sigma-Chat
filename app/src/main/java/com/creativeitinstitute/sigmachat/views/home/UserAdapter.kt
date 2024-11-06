package com.creativeitinstitute.sigmachat.views.home

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import coil.load
import com.bumptech.glide.Glide
import com.creativeitinstitute.sigmachat.R
import com.creativeitinstitute.sigmachat.databinding.ItemUserBinding
import com.creativeitinstitute.sigmachat.utils.User

class UserAdapter(val userListener:UserListener) : ListAdapter<User,UserViewHolder>(COMPARATOR) {

    private lateinit var context: Context

    interface UserListener{
        fun userItemClick(user: User)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {

        context = parent.context

        return UserViewHolder(ItemUserBinding.inflate(LayoutInflater.from(parent.context),parent,false))


    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {

        getItem(position)?.let {
            holder.binding.apply {
                fullName.text = it.fullName
                userBio.text = it.bio
                email.text = it.email

//                profileImage.load(it.profileImage)

                Glide.with(context).load(it.profileImage).placeholder(R.drawable.placeholder)
                    .into(profileImage)


                holder.itemView.setOnClickListener { _ ->
                    userListener.userItemClick(it)
                }
            }
        }


    }
    companion object{

        val COMPARATOR = object :DiffUtil.ItemCallback<User>(){
            override fun areItemsTheSame(oldItem: User, newItem: User): Boolean {
                return oldItem ==newItem

            }

            override fun areContentsTheSame(oldItem: User, newItem: User): Boolean {
                return oldItem.userId == newItem.userId
            }
        }
    }
}


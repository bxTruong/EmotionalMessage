package com.truongbx.emotionalmessage.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.truongbx.emotionalmessage.R
import com.truongbx.emotionalmessage.databinding.ItemUserOnlineBinding
import com.truongbx.emotionalmessage.model.User

class UserOnlineAdapter(
    private val context: Context,
    private val users: ArrayList<User>
) : RecyclerView.Adapter<UserOnlineAdapter.UserHolder>() {

    private lateinit var binding: ItemUserOnlineBinding;

    class UserHolder(binding: ItemUserOnlineBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserHolder {
        binding = DataBindingUtil.inflate(
            LayoutInflater.from(context),
            R.layout.item_user_online,
            parent, false
        )
        return UserHolder(binding);
    }

    override fun getItemCount(): Int {
        return users.size
    }

    override fun onBindViewHolder(holder: UserHolder, position: Int) {
        val user = users[position]
        binding.user = user
    }
}
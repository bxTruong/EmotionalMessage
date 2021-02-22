package com.truongbx.emotionalmessage.ui.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.truongbx.emotionalmessage.databinding.ItemSearchBinding
import com.truongbx.emotionalmessage.model.User

class SearchAdapter(
    private val users: ArrayList<User>
) : RecyclerView.Adapter<SearchAdapter.SearchHolder>() {

    inner class SearchHolder(val binding: ItemSearchBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(user: User) {
            binding.user = user
            binding.imageUrl = ""
            binding.root.setOnClickListener {
                val action = SearchFragmentDirections.actionSearchFragmentToMessageFragment(
                    user.uid
                )
                binding.root.findNavController().navigate(action)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = SearchHolder(
        ItemSearchBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
    )


    override fun getItemCount() = users.size

    override fun onBindViewHolder(holder: SearchAdapter.SearchHolder, position: Int) {
        holder.bind(users[position])
    }
}
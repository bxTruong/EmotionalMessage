package com.truongbx.emotionalmessage.ui.message_list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.truongbx.emotionalmessage.databinding.ItemMessageListBinding
import com.truongbx.emotionalmessage.model.MessageList

class MessageListAdapter(
    private val messageLists: ArrayList<MessageList>,
    private val messageListSetValue: MessageListSetValue
) : RecyclerView.Adapter<MessageListAdapter.MessageListHolder>() {

    inner class MessageListHolder(val binding: ItemMessageListBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: MessageList) {

            messageListSetValue.setMessage(item.message_id, binding)
            messageListSetValue.setUserName(item.uid, binding)
            binding.root.setOnClickListener {
                val action =
                    MessageListFragmentDirections.actionMessageListFragmentToMessageFragment(
                        item.uid
                    )
                binding.root.findNavController().navigate(action)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = MessageListHolder(
        ItemMessageListBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
    )

    override fun getItemCount() = messageLists.size

    override fun onBindViewHolder(holder: MessageListHolder, position: Int) {
        holder.bind(messageLists[position])
    }

}
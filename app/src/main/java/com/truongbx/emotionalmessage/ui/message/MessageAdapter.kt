@file:Suppress("UNREACHABLE_CODE")

package com.truongbx.emotionalmessage.ui.message

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.truongbx.emotionalmessage.databinding.ItemMessageLeftBinding
import com.truongbx.emotionalmessage.databinding.ItemMessageRightBinding
import com.truongbx.emotionalmessage.helper.InitFirebase.reference
import com.truongbx.emotionalmessage.model.Message
import com.truongbx.emotionalmessage.model.User

class MessageAdapter(
    var messages: ArrayList<Message>, var listener: Listener
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        const val MESSAGE_RIGHT = 1
        const val MESSAGE_LEFT = 2
    }

    interface Listener {
        fun onClickMessage(message: Message, isImage: Boolean)
        fun onLongClickMessage(v: View?, message: Message): Boolean
    }

    inner class MessageRightHolder(val binding: ItemMessageRightBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Message) {
            binding.message = item
            binding.isExpand =
                item.expand == true || item.notification == "Gửi không thành công" == true
            binding.listener = listener
            binding.isImage = item.url != "null" == true
        }
    }

    inner class MessageLeftHolder(val binding: ItemMessageLeftBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Message) {
            binding.message = item
            binding.isExpand = item.expand == true
            binding.listener = listener
            binding.seen = if (item.isseen) "Đã xem" else "Chưa xem"
            binding.isImage = item.url != "null" == true
            reference.child("Users").child(item.receiver)
                .addValueEventListener(object : ValueEventListener {
                    override fun onCancelled(error: DatabaseError) {
                    }

                    override fun onDataChange(snapshot: DataSnapshot) {
                        val user: User = snapshot.getValue(User::class.java)!!
                        binding.reciever = user
                    }

                })
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == MESSAGE_RIGHT) {
            MessageRightHolder(
                ItemMessageRightBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
        } else {
            MessageLeftHolder(
                ItemMessageLeftBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
        }
    }

    override fun getItemCount() = messages.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is MessageRightHolder) {
            holder.bind(messages[position])
        } else if (holder is MessageLeftHolder) {
            holder.bind(messages[position])
        }
    }

    override fun getItemViewType(position: Int): Int {
        val firebaseUser = FirebaseAuth.getInstance().currentUser
        return if (messages[position].sender == firebaseUser!!.uid) {
            MESSAGE_RIGHT
        } else {
            MESSAGE_LEFT
        }
    }

}


package com.truongbx.emotionalmessage.ui.message_list

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.truongbx.emotionalmessage.R
import com.truongbx.emotionalmessage.databinding.FragmentMessageListBinding
import com.truongbx.emotionalmessage.databinding.ItemMessageListBinding
import com.truongbx.emotionalmessage.helper.Helper
import com.truongbx.emotionalmessage.helper.InitFirebase
import com.truongbx.emotionalmessage.model.Message
import com.truongbx.emotionalmessage.model.MessageList
import com.truongbx.emotionalmessage.model.User


class MessageListFragment : Fragment() {
    private var firebaseUser = InitFirebase.firebaseUser
    private var reference = InitFirebase.reference
    private val messageLists = ArrayList<MessageList>()
    private lateinit var binding: FragmentMessageListBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_message_list, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        activity?.let { Helper(it).statusBarGradient() }
        binding.messageList = this
        initData()
    }

    private fun initData() {
        reference.child("ChatList").child(firebaseUser!!.uid)
            .addValueEventListener(object : ValueEventListener {
                override fun onCancelled(error: DatabaseError) {
                    Log.e("ERROR MESSAGE LIST", error.toString())
                }

                override fun onDataChange(p0: DataSnapshot) {
                    messageLists.clear()
                    for (snapshot: DataSnapshot in p0.children) {
                        var messageList: MessageList = snapshot.getValue(MessageList::class.java)!!
                        messageLists.add(messageList)
                    }
                    initRecyclerView(messageLists)
                }
            })
    }

    private fun initRecyclerView(messageLists: ArrayList<MessageList>) {
        binding.rcMessageList.adapter =
            MessageListAdapter(
                messageLists,
                object :
                    MessageListSetValue {
                    override fun setUserName(uid: String, binding: ItemMessageListBinding) {
                        InitFirebase.reference.child("Users").child(uid)
                            .addValueEventListener(object : ValueEventListener {
                                override fun onCancelled(error: DatabaseError) {
                                }

                                override fun onDataChange(p0: DataSnapshot) {
                                    if (p0.exists()) {
                                        binding.user = p0.getValue(User::class.java)
                                    }
                                }

                            })
                    }

                    override fun setMessage(message_id: String, binding: ItemMessageListBinding) {
                        InitFirebase.reference.child("Chats").child(message_id)
                            .addValueEventListener(object : ValueEventListener {
                                override fun onCancelled(error: DatabaseError) {
                                }

                                override fun onDataChange(p0: DataSnapshot) {
                                    if (p0.exists()) {
                                        binding.message = p0.getValue(Message::class.java)
                                    }
                                }

                            })
                    }

                })
    }

    fun openSearch() {
        val action =
            MessageListFragmentDirections.actionMessageListFragmentToSearchFragment()
        findNavController().navigate(action)
    }

}
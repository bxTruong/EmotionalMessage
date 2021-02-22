package com.truongbx.emotionalmessage.ui.message

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnFocusChangeListener
import android.view.View.OnTouchListener
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.Task
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.StorageTask
import com.google.firebase.storage.UploadTask
import com.truongbx.emotionalmessage.R
import com.truongbx.emotionalmessage.databinding.FragmentMessageBinding
import com.truongbx.emotionalmessage.helper.Helper
import com.truongbx.emotionalmessage.helper.InitFirebase
import com.truongbx.emotionalmessage.model.Message
import com.truongbx.emotionalmessage.model.MessageList
import com.truongbx.emotionalmessage.model.User
import kotlinx.android.synthetic.main.fragment_message.*
import java.util.*

@Suppress("DEPRECATION", "UNREACHABLE_CODE", "ALWAYS_NULL", "NAME_SHADOWING")
class MessageFragment : Fragment() {
    val args: MessageFragmentArgs by navArgs()
    private var firebaseUser = InitFirebase.firebaseUser
    private var reference = InitFirebase.reference
    private val messages = ArrayList<Message>()
    private var adapter: MessageAdapter? = null
    private lateinit var binding: FragmentMessageBinding
    private lateinit var receiver: String
    private lateinit var helper: Helper

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_message, container, false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        receiver = args.uid

        helper = activity?.let { Helper(it) }!!
        helper.statusBarWhite()
        binding.helper = helper
        binding.message = this

        infoReceiver(receiver)
        setUpEditText()
        retrieveMessages(firebaseUser!!.uid, receiver)
    }

    private fun infoReceiver(receiver: String) {
        reference.child("Users")
            .child(receiver)
            .addValueEventListener(object : ValueEventListener {
                override fun onCancelled(error: DatabaseError) {
                }

                override fun onDataChange(p0: DataSnapshot) {
                    val user = p0.getValue(User::class.java)
                    tvNameReceiverMessage.text = user?.full_name
                    activity?.let {
                        Helper(it).loadImage(
                            user?.avatar.toString(),
                            imgAvatarMessage
                        )
                    }
                }
            })
    }

    fun checkSendMessage(receiver: String) {
        if (edtSendMessage.text.toString() != "") {
            sendMessage(firebaseUser!!.uid, receiver, edtSendMessage.text.toString())
            edtSendMessage.text = null
        }
    }

    private fun sendMessage(sender: String, receiver: String, messageSend: String) {
        val messageKey = reference.push().key
        val message =
            Message(
                sender,
                messageSend,
                receiver,
                false,
                helper.getTime(),
                helper.getDate(),
                "null",
                "null",
                messageKey.toString()
            )
        addMessageList(message)
    }

    private fun addMessageList(message: Message) {
        reference.child("Chats")
            .child(message.messageId)
            .setValue(message.toMap())
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    reference.child("Chats").child(message.messageId).child("notification")
                        .setValue(activity?.resources?.getString(R.string.sent))

                    val chatsListReference =
                        FirebaseDatabase.getInstance().reference
                            .child("ChatList")
                            .child(firebaseUser!!.uid)
                            .child(receiver)

                    chatsListReference.addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onCancelled(error: DatabaseError) {
                        }

                        override fun onDataChange(snapshot: DataSnapshot) {
                            val messageList1 = MessageList(receiver, message.messageId)
                            chatsListReference.setValue(messageList1.toMap())

                            val chatsListReceiver =
                                FirebaseDatabase.getInstance().reference
                                    .child("ChatList")
                                    .child(receiver)
                                    .child(firebaseUser!!.uid)
                            val messageList = MessageList(firebaseUser!!.uid, message.messageId)
                            chatsListReceiver.setValue(messageList.toMap())
                        }
                    })
                } else {
                    reference.child("Chats").child(message.messageId).child("notification")
                        .setValue(activity?.resources?.getString(R.string.unsuccessful_sent))
                }
            }
    }

    private fun retrieveMessages(senderId: String, receiverId: String) {
        val reference = FirebaseDatabase.getInstance().reference.child("Chats")

        reference.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                Log.e("ERROR GET MESSAGE", error.toString())
            }

            override fun onDataChange(p0: DataSnapshot) {
                messages.clear()
                for (snapshot: DataSnapshot in p0.children) {
                    val message = snapshot.getValue(Message::class.java)
                    if (message!!.receiver == senderId && message.sender == receiverId ||
                        message.receiver == receiverId && message.sender == senderId
                    ) {
                        messages.add(message)
                    }
                }
                if (messages.size == 0) {
                    binding.isShow = true
                } else {
                    binding.isShow = false
                    initRecyclerView(messages)
                }
            }

        })
    }

    private fun initRecyclerView(messages: ArrayList<Message>) {
        adapter = MessageAdapter(
            messages,
            object :
                MessageAdapter.Listener {
                override fun onClickMessage(message: Message, isImage: Boolean) {
                    if (!isImage) {
                        val index = messages.indexOf(message)
                        message.expand = !message.expand
                        adapter?.notifyItemChanged(index)
                    } else {
                        val action =
                            MessageFragmentDirections.actionMessageFragmentToShowImageFragment(
                                message.messageId
                            )
                        binding.root.findNavController().navigate(action)
                    }
                }

                override fun onLongClickMessage(v: View?, message: Message): Boolean {
                    DialogMessage(
                        activity!!
                    ).openBottomSheetDialog(message, firebaseUser!!.uid)
                    return false
                }


            })
        binding.rcMessage.adapter = adapter
        (binding.rcMessage.layoutManager as LinearLayoutManager).stackFromEnd = true

    }

    private fun setUpEditText() {
        rcMessage.setOnTouchListener(setOnTouchRecyclerVIew)
        edtSendMessage.onFocusChangeListener = setOnFocusEditText
        edtSendMessage.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}

            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}

            @SuppressLint("ResourceAsColor")
            override fun afterTextChanged(editable: Editable) {
                tvSendMessage.setTextColor(
                    if (editable.toString().trim().isEmpty())
                        resources.getColor(R.color.gray_text)
                    else
                        resources.getColor(R.color.main_color)
                )
            }
        })
    }

    fun choseImage() {
        val intent = Intent()
        intent.action = Intent.ACTION_GET_CONTENT
        intent.type = "image/*"
        startActivityForResult(
            Intent.createChooser(
                intent,
                activity?.resources?.getString(R.string.choose_an_image)
            ), 888
        )
    }

    @SuppressLint("ClickableViewAccessibility")
    private var setOnTouchRecyclerVIew =
        OnTouchListener { _, _ ->
            edtSendMessage.maxLines = 1
            edtSendMessage.clearFocus()
            false
        }

    private var setOnFocusEditText =
        OnFocusChangeListener { view, _ ->
            edtSendMessage.maxLines = 3
            val imm =
                context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
            imm!!.hideSoftInputFromWindow(view.windowToken, 0)
        }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 888 && resultCode == Activity.RESULT_OK && data != null && data.data != null) {

            val fileUri = data.data
            val storageReference = InitFirebase.storageReference.child("Chat Images")
            val messageId = reference.push().key
            val filePath = storageReference.child("$messageId.jpg")
            val uploadTask: StorageTask<*>
            uploadTask = filePath.putFile(fileUri!!)
            uploadTask.continueWithTask(Continuation<UploadTask.TaskSnapshot, Task<Uri>> { task ->
                if (task.isSuccessful) {
                    task.exception?.let {
                        throw it
                    }
                }
                return@Continuation filePath.downloadUrl
            }).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val downloadUrl = task.result
                    val url = downloadUrl.toString()
                    val message = Message(
                        firebaseUser!!.uid,
                        activity?.resources?.getString(R.string.sent_an_image)!!,
                        receiver,
                        false,
                        helper.getTime(),
                        helper.getDate(),
                        url,
                        "",
                        messageId.toString()
                    )
                    addMessageList(message)
                }
            }
        }
    }

}
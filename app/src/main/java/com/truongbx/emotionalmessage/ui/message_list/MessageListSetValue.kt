package com.truongbx.emotionalmessage.ui.message_list

import com.truongbx.emotionalmessage.databinding.ItemMessageListBinding

interface MessageListSetValue {
    fun setUserName(uid: String, binding: ItemMessageListBinding)
    fun setMessage(message_id: String, binding: ItemMessageListBinding)
}
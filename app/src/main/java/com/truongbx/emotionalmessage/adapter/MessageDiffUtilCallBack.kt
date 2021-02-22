package com.truongbx.emotionalmessage.adapter

import androidx.recyclerview.widget.DiffUtil
import com.truongbx.emotionalmessage.model.Message

class MessageDiffUtilCallBack(
    var messagesOld: ArrayList<Message>,
    var messagesNew: ArrayList<Message>
) : DiffUtil.Callback() {

    var messageNew = ArrayList<Message>()

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return messagesNew[newItemPosition].messageId==messagesOld[oldItemPosition].messageId
    }

    override fun getOldListSize(): Int {
        return messagesOld.size
    }

    override fun getNewListSize(): Int {
        return messagesNew.size
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldUserName = messagesOld[oldItemPosition].message
        val newUserName = messagesNew[newItemPosition].message
        return oldUserName == newUserName
    }

    override fun getChangePayload(oldItemPosition: Int, newItemPosition: Int): Any? {
        return super.getChangePayload(oldItemPosition, newItemPosition)
    }
}
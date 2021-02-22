package com.truongbx.emotionalmessage.model

import com.google.firebase.database.Exclude
import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class Message(
    var sender: String = "",
    var message: String = "",
    var receiver: String = "",
    var isseen: Boolean = false,
    var time: String = "",
    var date: String = "",
    var url: String = "",
    var notification: String = "",
    var messageId: String = "",
    var expand: Boolean = false
)  {
    @Exclude
    fun toMap(): Map<String, Any?> {
        return mapOf(
            "sender" to sender,
            "message" to message,
            "receiver" to receiver,
            "isseen" to isseen,
            "time" to time,
            "date" to date,
            "url" to url,
            "notification" to notification,
            "messageId" to messageId
        )
    }
}

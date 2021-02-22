package com.truongbx.emotionalmessage.model

import com.google.firebase.database.Exclude
import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class MessageList(
    var uid: String = "",
    var message_id: String = ""
) {
    @Exclude
    fun toMap(): Map<String, Any?> {
        return mapOf(
            "uid" to uid,
            "message_id" to message_id
        )
    }
}
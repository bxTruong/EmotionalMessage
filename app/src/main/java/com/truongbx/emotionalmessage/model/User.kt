package com.truongbx.emotionalmessage.model

import com.google.firebase.database.Exclude
import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class User(
    var username: String = "",
    var password: String = "",
    var full_name: String = "",
    var birth_day: String = "",
    var phone_number: String = "",
    var gender: String = "",
    var avatar: String = "",
    var uid: String = ""
) {
    @Exclude
    fun toMap(): Map<String, Any?> {
        return mapOf(
            "username" to username,
            "password" to password,
            "full_name" to full_name,
            "birth_day" to birth_day,
            "phone_number" to phone_number,
            "gender" to gender,
            "avatar" to avatar,
            "uid" to uid
        )
    }
}



package com.truongbx.emotionalmessage.model

data class NotificationData(
    val user: String = "",
    val icon: Int = 0,
    val body: String = "",
    val title: String = "",
    val message: String = ""
)
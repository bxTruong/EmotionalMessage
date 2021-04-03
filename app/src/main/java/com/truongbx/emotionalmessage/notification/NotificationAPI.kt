package com.truongbx.emotionalmessage.notification

import com.truongbx.emotionalmessage.model.PushNotification
import com.truongbx.emotionalmessage.notification.Constants.CONTENT_TYPE
import com.truongbx.emotionalmessage.notification.Constants.SERVER_KEY
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface NotificationAPI {

    @Headers(
        "Content-Type: $CONTENT_TYPE",
        "Authorization: key=$SERVER_KEY"
    )
    @POST("fcm/send")
    fun sendNotification(@Body pushNotification: PushNotification): Call<MyResponse>
}
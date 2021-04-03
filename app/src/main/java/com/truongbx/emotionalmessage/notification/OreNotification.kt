package com.truongbx.emotionalmessage.notification

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.ContextWrapper
import android.net.Uri
import android.os.Build
import androidx.annotation.RequiresApi

@RequiresApi(Build.VERSION_CODES.O)
class OreNotification(base: Context?) : ContextWrapper(base) {

    companion object {
        private const val ID: String = "com.truongbx.emotionalmessage"
        private const val NAME: String = "EmotionalMessage"
    }

    private var notificationManager: NotificationManager? = null

    init {
        createChannel()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createChannel() {
        val notificationChannel =
            NotificationChannel(ID, NAME, NotificationManager.IMPORTANCE_DEFAULT)
        notificationChannel.enableLights(true)
        notificationChannel.enableVibration(true)
        notificationChannel.lockscreenVisibility = Notification.VISIBILITY_PRIVATE
        getManager.createNotificationChannel(notificationChannel)
    }

    val getManager: NotificationManager get(){
        if (notificationManager == null) {
            notificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager?
        }
        return notificationManager!!
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getOreoNotifications(
        title: String,
        body: String,
        pIntent: PendingIntent,
        soundUri: Uri,
        icon: String
    ): Notification.Builder {
        return Notification.Builder(applicationContext, ID)
            .setContentIntent(pIntent)
            .setContentTitle(title)
            .setContentText(body)
            .setSound(soundUri)
            .setAutoCancel(true)
            .setSmallIcon(icon.toInt())
    }

}
package com.truongbx.emotionalmessage.notification

import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.truongbx.emotionalmessage.activity.MainActivity
import com.truongbx.emotionalmessage.helper.InitFirebase.firebaseUser

class MyFirebaseMessagingService : FirebaseMessagingService() {

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        val sharedPref = getSharedPreferences("PREFS", Context.MODE_PRIVATE)
        val currentOnlineUser = sharedPref.getString("currentUser", "none")

//        val sented: String = remoteMessage.data["sented"]!!
//        Log.e("Asdsaas",sented)
        val user: String = remoteMessage.data["user"]!!
        val fUser = firebaseUser
        if (fUser != null ) {
            if (fUser.uid != user) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    sendOreoNotification(remoteMessage)
                } else {
                    sendNormalNotification(remoteMessage)
                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun sendOreoNotification(remoteMessage: RemoteMessage) {
        val user: String = remoteMessage.data["user"]!!
        val icon: String = remoteMessage.data["icon"]!!
        val body: String = remoteMessage.data["body"]!!
        val title: String = remoteMessage.data["title"]!!
        val message: String = remoteMessage.data["message"]!!

//        val notification: RemoteMessage.Notification = remoteMessage.notification!!

        val i = user.replace("[\\D]".toRegex(), "").toInt()
        val intent = Intent(this, MainActivity::class.java)
        val bundle = Bundle()
        bundle.putString("userId", user)
        intent.putExtras(bundle)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)

        val pendingIntent = PendingIntent.getActivity(this, i, intent, PendingIntent.FLAG_ONE_SHOT)
        val defaultSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val oreNotification = OreNotification(this)
        val builder: Notification.Builder =
            oreNotification.getOreoNotifications(title, body, pendingIntent, defaultSound, icon)
        var j = 0
        if (i > 0) {
            j = i
        }

        oreNotification.getManager.notify(j, builder.build())
    }

    private fun sendNormalNotification(remoteMessage: RemoteMessage) {
        val user: String = remoteMessage.data["user"]!!
        val icon: String = remoteMessage.data["icon"]!!
        val body: String = remoteMessage.data["body"]!!
        val title: String = remoteMessage.data["title"]!!
        val message: String = remoteMessage.data["message"]!!

        val i = user.replace("[\\D]".toRegex(), "").toInt()
        val intent = Intent(this, MainActivity::class.java)
        val bundle = Bundle()
        bundle.putString("userId", user)
        intent.putExtras(bundle)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)

        val pendingIntent = PendingIntent.getActivity(this, i, intent, PendingIntent.FLAG_ONE_SHOT)
        val defaultSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val builder: NotificationCompat.Builder = NotificationCompat.Builder(this)
            .setSmallIcon(icon.toInt())
            .setContentTitle(title)
            .setContentText(body)
            .setAutoCancel(true)
            .setSound(defaultSound)
            .setContentIntent(pendingIntent)

        val noti=getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        var j = 0
        if (i > 0) {
            j = i
        }
        noti.notify(j, builder.build())
    }

}
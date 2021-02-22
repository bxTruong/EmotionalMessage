package com.truongbx.emotionalmessage.helper

import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.view.View
import android.view.View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
import android.view.WindowManager
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.makeramen.roundedimageview.RoundedImageView
import com.truongbx.emotionalmessage.R
import java.util.*

object InitFirebase {
    var reference = FirebaseDatabase.getInstance().reference
    var firebaseUser = FirebaseAuth.getInstance().currentUser
    val storageReference = FirebaseStorage.getInstance().reference
}

class Helper(var activity: Activity) {

    fun statusBarGradient() {
        activity.window?.apply {
            clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            statusBarColor = Color.TRANSPARENT
        }
    }

    fun statusBarWhite() {
        activity.window?.decorView?.systemUiVisibility = SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        activity.window?.statusBarColor =
            activity.let { ContextCompat.getColor(it, R.color.white) }
    }

    fun statusBarBlack() {
        activity.window?.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        activity.window?.decorView?.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        activity.window?.statusBarColor =
            activity.let { ContextCompat.getColor(it, R.color.black_text) }
    }

    fun loadImage(url: String, roundImg: RoundedImageView) {
        Glide.with(activity)
            .load(url)
            .into(roundImg)
    }

    fun onBackPress() {
        activity.onBackPressed()
    }

    fun getDate(): String {
        val calendar = Calendar.getInstance()
        val year = calendar[Calendar.YEAR]
        val month = calendar[Calendar.MONTH]
        val day = calendar[Calendar.DAY_OF_MONTH]
        return if (day < 10 && month >= 9) {
            "0$day/${month + 1}/$year"
        } else if (day < 10 && month < 9) {
            "0$day/0${month + 1}/$year"
        } else if (day > 10 && month < 9) {
            "$day/0${month + 1}/$year"
        } else {
            "$day/${month + 1}/$year"
        }
    }

    fun getTime(): String {
        val calendar = Calendar.getInstance()
        val hour = calendar[Calendar.HOUR_OF_DAY]
        val minute = calendar[Calendar.MINUTE]
        return if (hour < 10 && minute < 10) {
            "0$hour:0$minute"
        } else if (hour < 10 && minute > 10) {
            "0$hour:$minute"
        } else if (hour > 10 && minute < 10) {
            "$hour:0$minute"
        } else {
            "$hour:$minute"
        }
    }

}

fun Context.toast(message: String) = Toast.makeText(this, message, Toast.LENGTH_SHORT).show()


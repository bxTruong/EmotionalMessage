package com.truongbx.emotionalmessage.helper

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color
import android.view.View
import android.view.View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import androidx.navigation.NavDirections
import androidx.navigation.NavOptions
import androidx.navigation.Navigation
import androidx.navigation.findNavController
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

    private fun statusBarWhite() {
        activity.window?.decorView?.systemUiVisibility = SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        activity.window?.statusBarColor =
            activity.let { ContextCompat.getColor(it, R.color.white) }
    }

    fun statusBarBlack() {
        activity.window?.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        activity.window?.decorView?.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        activity.window?.statusBarColor =
            activity.let { ContextCompat.getColor(it, R.color.bg_show_image) }
    }

    fun setStatusBar(fragment: String) {
        if (readTheme(activity) == true && fragment == "Message") statusBarBlack()
        else statusBarWhite()
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

fun navigate(view: View, navDirections: NavDirections) {
    view.findNavController().navigate(navDirections)
}

fun navigateChangeStart(view: View, id: Int, idAction: Int) {
    val navOptions = NavOptions.Builder()
        .setPopUpTo(id, true)
        .build()
    Navigation.findNavController(view)
        .navigate(idAction, null, navOptions)
}

private const val THEME = "THEME"
private const val NIGHT_MODE = "NIGHT_MODE"

@SuppressLint("ApplySharedPref")
fun saveTheme(activity: Activity, nightMode: Boolean) {
    val sharedPreferences: SharedPreferences? =
        activity.getSharedPreferences(THEME, Context.MODE_PRIVATE)
    val editor = sharedPreferences?.edit()
    editor?.putBoolean(NIGHT_MODE, nightMode)
    editor?.commit()
}

fun readTheme(activity: Activity): Boolean? {
    val sharedPreferences =
        activity.getSharedPreferences(THEME, Context.MODE_PRIVATE)
    val theme = sharedPreferences?.getBoolean(NIGHT_MODE, false)!!
    if (theme) AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
    else AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
    return theme
}


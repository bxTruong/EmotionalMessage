package com.truongbx.emotionalmessage.helper

import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.makeramen.roundedimageview.RoundedImageView
import com.truongbx.emotionalmessage.R

@BindingAdapter("android:image_notification")
fun imgNotification(view: ImageView, isNotification: String) {
    Glide.with(view.context)
        .load("")
        .error(if (isNotification == view.context.resources.getString(R.string.sent)) R.drawable.ic_double_tick else R.drawable.ic_warning_message)
        .into(view)
}

@BindingAdapter("android:src_glide")
fun loadImageGlide(view: RoundedImageView, imageUrl: String?) {
    Glide.with(view.context)
        .load(imageUrl)
        .into(view)
}

@BindingAdapter("android:font_custom")
fun TextView.font(type: FontTypes) {
    try {
        typeface = ResourcesCompat.getFont(context, type.fontRes)
    } catch (e: Exception) {
        e.printStackTrace()
    }
}
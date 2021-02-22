package com.truongbx.emotionalmessage.ui.message

import android.app.Activity
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Handler
import android.view.Gravity
import android.view.LayoutInflater
import android.view.Window
import android.view.WindowManager
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import com.truongbx.emotionalmessage.R
import com.truongbx.emotionalmessage.databinding.DialogBottomSheetMessageBinding
import com.truongbx.emotionalmessage.helper.InitFirebase
import com.truongbx.emotionalmessage.helper.toast
import com.truongbx.emotionalmessage.model.Message

class DialogMessage(var activity: Activity) {

    private lateinit var bottomSheetDialog: AlertDialog

    fun openBottomSheetDialog(message: Message, uid: String) {
        val builder = AlertDialog.Builder(activity)
        val binding: DialogBottomSheetMessageBinding = DataBindingUtil.inflate(
            LayoutInflater.from(activity),
            R.layout.dialog_bottom_sheet_message,
            null,
            false
        )
        builder.setView(binding.root)

        binding.check = message.sender == uid == true
        binding.checkImage = message.url == "null" == true
        binding.message = message
        binding.dialogMessage = this

        binding.btsMessageLeft.message = message
        binding.btsMessageLeft.isExpand = false
        binding.btsMessageLeft.isImage = message.url != "null" == true

        binding.btsMessageRight.message = message
        binding.btsMessageRight.isExpand = false
        binding.btsMessageRight.isImage = message.url != "null" == true

        bottomSheetDialog = builder.create()
        bottomSheetDialog.window?.attributes?.windowAnimations = R.style.BottomSheetAnimation
        bottomSheetDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val window: Window = bottomSheetDialog.window!!
        val wlp = window.attributes
        wlp.gravity = Gravity.BOTTOM
        wlp.flags = wlp.flags and WindowManager.LayoutParams.FLAG_DIM_BEHIND.inv()
        window.attributes = wlp
        bottomSheetDialog.show()
    }

    fun copyMessage(message: Message) {
        val clipboardManager: ClipboardManager =
            activity.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clipData = ClipData.newPlainText(
            "Message",
            message.message
        )
        clipboardManager.setPrimaryClip(clipData)
        closeBottomSheetDialog(activity.resources.getString(R.string.success_copy))
    }

    fun undeveloped() {
        closeBottomSheetDialog(activity.resources.getString(R.string.undeveloped))
    }

    fun deleteMessage(message: Message) {
        Handler().postDelayed({
            bottomSheetDialog.dismiss()
            activity.toast(activity.resources.getString(R.string.have_removed))
            InitFirebase.reference.child("Chats").child(message.messageId).removeValue()
        }, 300)
    }

    private fun closeBottomSheetDialog(toast: String) {
        Handler().postDelayed({
            bottomSheetDialog.dismiss()
            activity.toast(toast)
        }, 300)
    }
}
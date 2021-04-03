package com.truongbx.emotionalmessage.ui.setting

import android.app.Activity
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import com.google.firebase.auth.FirebaseAuth
import com.truongbx.emotionalmessage.R
import com.truongbx.emotionalmessage.databinding.DialogLogOutBinding
import com.truongbx.emotionalmessage.helper.InitFirebase
import com.truongbx.emotionalmessage.helper.navigateChangeStart

class DialogLogOut(var activity: Activity, var view: View) {
    private lateinit var alertDialog: AlertDialog
    private lateinit var binding: DialogLogOutBinding
    fun openDialog() {
        val builder = AlertDialog.Builder(activity)
        binding = DataBindingUtil.inflate(
            LayoutInflater.from(activity),
            R.layout.dialog_log_out,
            null,
            false
        )
        builder.setView(binding.root)

        binding.dialogLogOut = this

        alertDialog = builder.create()
        alertDialog.window?.attributes?.windowAnimations = R.style.DialogAnimation
        alertDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        alertDialog.show()
    }

    fun logOut() {
        alertDialog.dismiss()
        FirebaseAuth.getInstance().signOut()
        navigateChangeStart(
            view,
            R.id.settingFragment,
            R.id.action_settingFragment_to_loginFragment
        )
    }

    fun cancel() {
        alertDialog.dismiss()
    }
}
package com.truongbx.emotionalmessage.ui.setting

import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.truongbx.emotionalmessage.BuildConfig
import com.truongbx.emotionalmessage.R
import com.truongbx.emotionalmessage.databinding.FragmentSettingBinding
import com.truongbx.emotionalmessage.helper.Helper
import com.truongbx.emotionalmessage.helper.InitFirebase.firebaseUser
import com.truongbx.emotionalmessage.helper.InitFirebase.reference
import com.truongbx.emotionalmessage.helper.readTheme
import com.truongbx.emotionalmessage.helper.saveTheme
import com.truongbx.emotionalmessage.model.User
import kotlinx.android.synthetic.main.fragment_message.*

class SettingFragment : Fragment(R.layout.fragment_setting) {

    private lateinit var binding: FragmentSettingBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_setting, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.settingFragment = this
        binding.version = BuildConfig.VERSION_NAME
        activity?.let { Helper(it).statusBarGradient() }
        binding.btnSwitch.isChecked = activity?.let { readTheme(it) } == true
        infoReceiver()
    }

    private fun infoReceiver() {
        reference.child("Users")
            .child(firebaseUser!!.uid)
            .addValueEventListener(object : ValueEventListener {
                override fun onCancelled(error: DatabaseError) {
                }

                override fun onDataChange(p0: DataSnapshot) {
                    val user = p0.getValue(User::class.java)
                    binding.user = user
                }
            })
    }

    fun selectionDarkMode() {
        if (binding.btnSwitch.isChecked) {
            activity?.let { saveTheme(it, true) }
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else {
            activity?.let { saveTheme(it, false) }
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
    }

    fun onBackPress() {
        activity?.onBackPressed()
    }

    fun logOut() {
        activity?.let { DialogLogOut(it, binding.root).openDialog() }
    }

}
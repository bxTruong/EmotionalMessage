package com.truongbx.emotionalmessage.notification

import android.util.Log
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.iid.FirebaseInstanceIdService
import com.truongbx.emotionalmessage.helper.InitFirebase.firebaseUser
import com.truongbx.emotionalmessage.helper.InitFirebase.reference
import com.truongbx.emotionalmessage.model.Token

class MyFirebaseInstanceIDService : FirebaseInstanceIdService() {
    override fun onTokenRefresh() {
        val user = firebaseUser
        val refreshedToken = FirebaseInstanceId.getInstance().token
        Log.e("TOKEN", "Refreshed token: $refreshedToken")
        if (user != null) {
            updateToken(refreshedToken)
        }
    }

    private fun updateToken(refreshedToken: String?) {
        val token1 = refreshedToken?.let { Token(it) }
        reference.child("Tokens")
            .child(firebaseUser!!.uid)
            .setValue(token1?.toMap())
    }
}
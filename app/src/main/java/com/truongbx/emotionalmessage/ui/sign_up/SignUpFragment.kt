package com.truongbx.emotionalmessage.ui.sign_up

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.truongbx.emotionalmessage.R
import com.truongbx.emotionalmessage.helper.Helper
import kotlinx.android.synthetic.main.fragment_sign_up.*
import com.truongbx.emotionalmessage.model.User


class SignUpFragment : Fragment(R.layout.fragment_sign_up) {
    private lateinit var mAuth: FirebaseAuth
    private lateinit var refUsers: DatabaseReference
    private var firebaseUserID: String = ""
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mAuth = FirebaseAuth.getInstance()
        btnSignUp.setOnClickListener {
            val email = edtEmail.text.toString()
            val password = edtPassword.text.toString()
            val phone = edtPhone.text.toString()
            val name = edtFullName.text.toString()

            mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        firebaseUserID = mAuth.currentUser!!.uid
                        refUsers = FirebaseDatabase.getInstance().reference.child("Users")
                            .child(firebaseUserID)
                        val date: String = activity?.let { it1 -> Helper(it1).getDate() }!!
                        val user = User(
                            email,
                            password,
                            name,
                            date,
                            "0987654321",
                            "Nam",
                            "https://firebasestorage.googleapis.com/v0/b/emotionalmessagekotlin.appspot.com/o/Chat%20Images%2F-MUpfekOeWmHqhuYb8wP.jpg?alt=media&token=aeae0186-b4a6-40c5-8168-ce1d9001b27f",
                            firebaseUserID
                        )

                        refUsers.updateChildren(user.toMap()).addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                Toast.makeText(activity, "Success", Toast.LENGTH_SHORT).show()
                            }
                        }
                    } else {
                        Toast.makeText(
                            activity,
                            "${task.exception!!.message}",
                            Toast.LENGTH_SHORT
                        )
                            .show()
                    }
                }
        }
    }
}
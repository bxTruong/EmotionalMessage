package com.truongbx.emotionalmessage.ui.sign_up

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.truongbx.emotionalmessage.R
import kotlinx.android.synthetic.main.fragment_sign_up.*


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
            val date = edtDate.text.toString()

            mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        firebaseUserID = mAuth.currentUser!!.uid
                        refUsers = FirebaseDatabase.getInstance().reference.child("Users")
                            .child(firebaseUserID)
                        val userHashMap = HashMap<String, Any>()
                        userHashMap["uid"] = firebaseUserID
                        userHashMap["image"] =
                            "https://i.pinimg.com/564x/44/1a/46/441a4698742a1b87b655236d8ef1a7a8.jpg"
                        userHashMap["username"] = email
                        userHashMap["phone"] = phone
                        userHashMap["password"] = password
                        userHashMap["date"] = date

                        refUsers.updateChildren(userHashMap).addOnCompleteListener { task ->
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
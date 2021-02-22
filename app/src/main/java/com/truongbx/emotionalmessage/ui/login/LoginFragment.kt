package com.truongbx.emotionalmessage.ui.login

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.truongbx.emotionalmessage.R
import kotlinx.android.synthetic.main.fragment_login.*

class LoginFragment : Fragment(R.layout.fragment_login) {

    private lateinit var mAuth: FirebaseAuth

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mAuth = FirebaseAuth.getInstance()

        btnOpenSignUp.setOnClickListener {
            findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToSignUpFragment())
        }

        btnLogin.setOnClickListener {
            val email = edtEmailLogin.text.toString()
            val password = edtPasswordLogin.text.toString()

            mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToMessageListFragment())
                } else {

                    //The password is invalid or the user does not have a password.
                    //
                    //There is no user record corresponding to this identifier. The user may have been deleted.

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
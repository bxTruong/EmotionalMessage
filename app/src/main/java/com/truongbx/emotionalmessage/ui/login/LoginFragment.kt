package com.truongbx.emotionalmessage.ui.login

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import androidx.fragment.app.Fragment
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.addCallback
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.truongbx.emotionalmessage.R
import com.truongbx.emotionalmessage.databinding.FragmentLoginBinding
import com.truongbx.emotionalmessage.databinding.FragmentSettingBinding
import com.truongbx.emotionalmessage.helper.navigateChangeStart
import kotlinx.android.synthetic.main.fragment_login.*

class LoginFragment : Fragment() {

    private lateinit var binding: FragmentLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requireActivity().onBackPressedDispatcher.addCallback(this) {
            activity?.finish()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_login, container, false)
        return binding.root
    }

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
                    navigateChangeStart(
                        view,
                        R.id.loginFragment,
                        R.id.action_loginFragment_to_messageListFragment
                    )
                } else {
                    Toast.makeText(
                        activity,
                        "${task.exception!!.message}",
                        Toast.LENGTH_SHORT
                    )
                        .show()
                    Log.e("ERRR", "${task.exception!!.message}")
                }
            }
        }
    }


}
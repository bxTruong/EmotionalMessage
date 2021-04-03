package com.truongbx.emotionalmessage.ui.profile

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.truongbx.emotionalmessage.R
import com.truongbx.emotionalmessage.model.User
import kotlinx.android.synthetic.main.fragment_profile.*

class   ProfileFragment : Fragment(R.layout.fragment_profile) {

    var refUsers: DatabaseReference? = null
    var firebaseUser: FirebaseUser? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        firebaseUser = FirebaseAuth.getInstance().currentUser
        refUsers=FirebaseDatabase.getInstance().reference.child("Users").child(firebaseUser!!.uid)

        refUsers!!.addValueEventListener(object : ValueEventListener{
            override fun onCancelled(error: DatabaseError) {
            }

            override fun onDataChange(p0: DataSnapshot) {
                if(p0.exists()){
                   val user=p0.getValue(User::class.java)

                    Log.e("AAAB","${user?.uid}")
                }
            }

        })

    }

}
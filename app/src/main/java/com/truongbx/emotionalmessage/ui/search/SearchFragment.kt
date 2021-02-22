package com.truongbx.emotionalmessage.ui.search

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.truongbx.emotionalmessage.R
import com.truongbx.emotionalmessage.helper.Helper
import com.truongbx.emotionalmessage.model.User
import kotlinx.android.synthetic.main.fragment_search.*


class SearchFragment : Fragment(R.layout.fragment_search) {

    private val users = ArrayList<User>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        activity?.let { Helper(it).statusBarGradient() }

        setOnClickBack()
        edtSearch.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                searchUsers(s.toString())
            }
        })
    }

    private fun setOnClickBack() {
        btnBackToolbarSearch.setOnClickListener {
            activity?.onBackPressed()
        }
    }

    private fun initRecyclerView(users: ArrayList<User>) {
        rcSearch?.adapter = SearchAdapter(users)
    }

    private fun searchFirebase() {

        var firebaseUserId = FirebaseAuth.getInstance().currentUser!!.uid

        val refUsers = FirebaseDatabase.getInstance().reference.child("Users").child(firebaseUserId)

        refUsers.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
            }

            override fun onDataChange(p0: DataSnapshot) {
                users!!.clear()
                for (snapshot in p0.children) {
                    val user: User? = p0.getValue(User::class.java)
                    if ((user!!.uid) != firebaseUserId) {
                        users!!.add(user)
                    }
                }
            }
        })
    }

    private fun searchUsers(search: String) {
        var firebaseUserId = FirebaseAuth.getInstance().currentUser!!.uid
        val searchUser = FirebaseDatabase.getInstance().reference
            .child("Users")

        searchUser.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                Log.e("ERROR", error.message)
            }

            override fun onDataChange(p0: DataSnapshot) {
                users.clear()
                for (snapshot in p0.children) {
                    val user: User? = snapshot.getValue(User::class.java)
                    Log.e("USER", "${user?.full_name}")
                    if (search != "") {
                        if ((user?.uid) != firebaseUserId && user?.full_name!!
                                .startsWith(search)
                        ) {
                            user?.let { users?.add(it) }
                        }
                    }
                }
                users?.let { initRecyclerView(it) }
            }
        })
    }
}
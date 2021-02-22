package com.truongbx.emotionalmessage.ui.show_image

import android.app.DownloadManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.truongbx.emotionalmessage.R
import com.truongbx.emotionalmessage.databinding.FragmentShowImageBinding
import com.truongbx.emotionalmessage.helper.Helper
import com.truongbx.emotionalmessage.helper.InitFirebase
import com.truongbx.emotionalmessage.helper.toast
import com.truongbx.emotionalmessage.model.Message


class ShowImageFragment : Fragment() {
    private val args: ShowImageFragmentArgs by navArgs()
    private lateinit var binding: FragmentShowImageBinding
    private var downloadImage: Long = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_show_image, container, false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        FirebaseDatabase.getInstance().reference.child("Chats").child(args.messageId)
            .addValueEventListener(object : ValueEventListener {
                override fun onCancelled(error: DatabaseError) {
                }

                override fun onDataChange(p0: DataSnapshot) {
                    val message: Message = p0.getValue(Message::class.java)!!
                    binding.message = message
                }

            })

        val helper: Helper = activity?.let { Helper(it) }!!
        binding.helper = helper
        helper.statusBarBlack()
        binding.showImage = this

        activity?.registerReceiver(onDownloadComplete, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE))
    }

    fun clickDownloadImage(messageId: String) {
        val islandRef = InitFirebase.storageReference.child("Chat Images").child("${messageId}.jpg")

        islandRef.downloadUrl.addOnSuccessListener {
            activity?.toast(activity?.resources!!.getString(R.string.is_downloading))
            context?.let { it1 ->
                downloadImage(
                    it1,
                    messageId,
                    Environment.DIRECTORY_DOWNLOADS,
                    it.toString()
                )
            }
        }.addOnFailureListener { exception ->
            Log.e("ERROR DOWNLOAD", exception.toString())
        }
    }

    private fun downloadImage(
        context: Context,
        fileName: String,
        destinationDirectory: String?,
        url: String?
    ) {
        val downloadManager =
            context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        val uri: Uri = Uri.parse(url)
        val request = DownloadManager.Request(uri)
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
        request.setDestinationInExternalFilesDir(
            context,
            destinationDirectory,
            "$fileName.jpg"
        )
        downloadImage = downloadManager.enqueue(request)

    }

    private val onDownloadComplete: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)
            if (downloadImage == id) {
                activity?.resources?.getString(R.string.download_successful)?.let { activity?.toast(it) }
            }
        }
    }
}
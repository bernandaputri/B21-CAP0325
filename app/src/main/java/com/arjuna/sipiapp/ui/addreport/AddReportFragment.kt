package com.arjuna.sipiapp.ui.addreport

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.arjuna.sipiapp.R
import com.arjuna.sipiapp.UserPreferences
import com.arjuna.sipiapp.databinding.FragmentAddReportBinding
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import java.util.*
import kotlin.collections.HashMap

class AddReportFragment : Fragment() {

    private lateinit var binding: FragmentAddReportBinding
    private lateinit var firebaseFirestore: FirebaseFirestore
    private lateinit var imgUrl: String
    private lateinit var userPref: UserPreferences
    private lateinit var imgFilename: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAddReportBinding.inflate(layoutInflater, container, false)

        firebaseFirestore = FirebaseFirestore.getInstance()

        clearForm()

        binding.btnImage.setOnClickListener { selectImage() }

        binding.btnSend.setOnClickListener {
            validateForm()

        }

        return binding.root
    }

    private fun validateForm() {
        val title = binding.edtReportTitle.text.toString()
        val location = binding.edtLocation.text.toString()

        if (title.isEmpty()) {
            binding.edtReportTitle.error = "Harus diisi."
        }

        if (location.isEmpty()) {
            binding.edtLocation.error = "Harus diisi."
        }

        if (title.isNotEmpty() && location.isNotEmpty()) {
            postForm(title, location)
        }
    }

    private fun selectImage() {
        val pickImage = Intent(Intent.ACTION_GET_CONTENT)
        pickImage.type = "image/*"
        startActivityForResult(pickImage, 100)
    }

    private fun postForm(title: String, location: String) {
        userPref = UserPreferences(requireContext())
        val userModel = userPref.getUser()

        val reportId = firebaseFirestore.collection("reports").document().id
        val reportDb = firebaseFirestore.collection("reports").document(reportId)
        val report = HashMap<String, Any>()
        report["report_id"] = reportId
        report["title"] = title
        report["location"] = location
        report["reported_by"] = userModel.username.toString()
        report["imageUrl"] = imgUrl
        report["status"] = ""
        report["filename"] = imgFilename
        reportDb.set(report).addOnSuccessListener {
            clearForm()
        }

    }

    private fun clearForm() {
        with(binding) {
            edtReportTitle.setText("")
            edtLocation.setText("")
            imageView.setImageDrawable(null)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 100 && resultCode == Activity.RESULT_OK && data != null) {
            val imageUri = data?.data!!
            val imgBitmap: Bitmap = MediaStore.Images.Media.getBitmap(activity?.contentResolver, imageUri)

            val bitmapDrawable = BitmapDrawable(imgBitmap)
            binding.imageView.setImageDrawable(bitmapDrawable)

            uploadImageProcess()
        }
    }

    private fun uploadImageProcess() {
        imgFilename = UUID.randomUUID().toString()
        val firebaseStorage = FirebaseStorage.getInstance().getReference("/projectlist1/${imgFilename}.jpg")
        imgUrl = firebaseStorage.downloadUrl.toString()
    }
}
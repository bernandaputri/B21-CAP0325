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
import com.arjuna.sipiapp.databinding.FragmentAddReportBinding
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import java.util.*

class AddReportFragment : Fragment() {

    private lateinit var binding: FragmentAddReportBinding
    private lateinit var firebaseFirestore: FirebaseFirestore
    private lateinit var selectedImage: Uri

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAddReportBinding.inflate(layoutInflater, container, false)

        firebaseFirestore = FirebaseFirestore.getInstance()

        clearForm()

        binding.btnImage.setOnClickListener { selectImage() }

        binding.btnSend.setOnClickListener { postForm() }

        return binding.root
    }

    private fun selectImage() {
        val pickImage = Intent(Intent.ACTION_GET_CONTENT)
        pickImage.type = "image/*"
        startActivityForResult(pickImage, 100)
    }

    private fun postForm() {

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
            binding.btnImage.setBackgroundDrawable(bitmapDrawable)
        }
    }

    private fun uploadImageProcess() {
        val fileName = UUID.randomUUID().toString()
        val firebaseStorage = FirebaseStorage.getInstance().getReference("/projectlist1/$fileName")
    }
}
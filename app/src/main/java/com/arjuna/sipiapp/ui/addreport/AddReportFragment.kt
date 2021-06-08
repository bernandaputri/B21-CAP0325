package com.arjuna.sipiapp.ui.addreport

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.arjuna.sipiapp.R
import com.arjuna.sipiapp.UserPreferences
import com.arjuna.sipiapp.data.PredictResponse
import com.arjuna.sipiapp.databinding.FragmentAddReportBinding
import com.arjuna.sipiapp.network.ApiConfig
import com.arjuna.sipiapp.network.ApiService
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import kotlin.collections.HashMap

class AddReportFragment : Fragment() {

    private lateinit var binding: FragmentAddReportBinding
    private lateinit var firebaseFirestore: FirebaseFirestore
    private lateinit var imgUrl: String
    private lateinit var imageUri: Uri
    private lateinit var userPref: UserPreferences
    private lateinit var imgFilename: String
    private lateinit var damageStatus: String
    private lateinit var reportId: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAddReportBinding.inflate(layoutInflater, container, false)

        firebaseFirestore = FirebaseFirestore.getInstance()

        clearForm()

        binding.btnImage.setOnClickListener { selectImage() }

        binding.btnSend.setOnClickListener {
            binding.progressBar.visibility = View.VISIBLE
            uploadImageProcess()
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

    private fun postForm(title: String, location: String) {

        userPref = UserPreferences(requireContext())
        val userModel = userPref.getUser()

        reportId = firebaseFirestore.collection("reports").document().id
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
            binding.progressBar.visibility = View.GONE
            clearForm()
//            predict()
        }

    }

    private fun predict() {
        val client = ApiConfig.getApiService().predictObject(imgFilename)
        client.enqueue(object : Callback<PredictResponse> {
            override fun onResponse(
                call: Call<PredictResponse>,
                response: Response<PredictResponse>
            ) {
                val predictReport = response.body()
                val minorBuilding = predictReport?.minorBuilding.toString()
                val minorBridge = predictReport?.minorBridge.toString()
                val minorRoad = predictReport?.minorRoad.toString()
                val moderateBuilding = predictReport?.moderateBuilding.toString()
                val moderateBridge = predictReport?.moderateBridge.toString()
                val moderateRoad = predictReport?.moderateRoad.toString()
                val heavyBuilding = predictReport?.heavyBuilding.toString()
                val heavyBridge = predictReport?.heavyBridge.toString()
                val heavyRoad = predictReport?.heavyRoad.toString()

                if (minorBuilding == "1.0") {
                    damageStatus = "Bangunan rusak ringan."
                } else if (minorBridge == "1.0") {
                    damageStatus = "Jembatan rusak ringan."
                } else if (minorRoad == "1.0") {
                    damageStatus = "Jalan rusak ringan."
                } else if (moderateBuilding == "1.0") {
                    damageStatus = "Bagungan rusak sedang."
                } else if (moderateBridge == "1.0") {
                    damageStatus = "Jembatan rusak sedang."
                } else if (moderateRoad == "1.0") {
                    damageStatus = "Jalan rusak sedang."
                } else if (heavyBuilding == "1.0") {
                    damageStatus = "Bangunan rusak berat."
                } else if (heavyBridge == "1.0") {
                    damageStatus = "Jembatan rusak berat."
                } else if (heavyRoad == "1.0") {
                    damageStatus = "Jalan rusak berat."
                }

                updateStatus()
            }

            override fun onFailure(call: Call<PredictResponse>, t: Throwable) {
                Log.e("Post failed: ", t.message.toString())
            }

        })
    }

    private fun updateStatus() {
        userPref = UserPreferences(requireContext())

        val status = hashMapOf(
            "status" to damageStatus
        )

        firebaseFirestore.collection("reports").document(reportId).set(status)
    }

    private fun clearForm() {
        with(binding) {
            edtReportTitle.setText("")
            edtLocation.setText("")
            imageView.setImageDrawable(null)
        }
    }

    private fun selectImage() {
        val pickImage = Intent(Intent.ACTION_PICK)
        pickImage.type = "image/jpg"
        startActivityForResult(pickImage, 100)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 100 && resultCode == Activity.RESULT_OK && data != null) {
            imageUri = data?.data!!
            val imgBitmap = MediaStore.Images.Media.getBitmap(activity?.contentResolver, imageUri)

            val bitmapDrawable = BitmapDrawable(imgBitmap)
            binding.imageView.setImageDrawable(bitmapDrawable)
        }
    }

    private fun uploadImageProcess() {
        imgFilename = UUID.randomUUID().toString() + ".jpg"
        val firebaseStorage = FirebaseStorage.getInstance("gs://projectlist1").getReference("$imgFilename")

        firebaseStorage.putFile(imageUri)
            .addOnSuccessListener {
                firebaseStorage.downloadUrl.addOnSuccessListener {
                    imgUrl = it.toString()

                    validateForm()
                }
            }
    }
}
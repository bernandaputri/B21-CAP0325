package com.arjuna.sipiapp.ui.addreport

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import java.io.ByteArrayOutputStream
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
    private lateinit var imgString: String
    private lateinit var imgBitmap: Bitmap

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAddReportBinding.inflate(layoutInflater, container, false)

        firebaseFirestore = FirebaseFirestore.getInstance()

        clearForm()

        binding.btnPredict.visibility = View.INVISIBLE

        binding.btnImage.setOnClickListener { selectImage() }

        binding.btnSend.setOnClickListener {
            binding.progressBar.visibility = View.VISIBLE
//            predict()
            uploadImageProcess()
        }

        binding.btnPredict.setOnClickListener { process() }

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
        report["status"] = damageStatus
        report["filename"] = imgFilename
        reportDb.set(report).addOnSuccessListener {
            binding.progressBar.visibility = View.GONE
            clearForm()
        }

    }

    private fun predict() {

        val client = ApiConfig.getApiService().predictObject(imgString)
        client.enqueue(object : Callback<PredictResponse> {
            override fun onResponse(
                call: Call<PredictResponse>,
                response: Response<PredictResponse>
            ) {
                val predictReport = response.body()
                val minorBuilding = predictReport?.minorBuilding
                val minorBridge = predictReport?.minorBridge
                val minorRoad = predictReport?.minorRoad
                val moderateBuilding = predictReport?.moderateBuilding
                val moderateBridge = predictReport?.moderateBridge
                val moderateRoad = predictReport?.moderateRoad
                val heavyBuilding = predictReport?.heavyBuilding
                val heavyBridge = predictReport?.heavyBridge
                val heavyRoad = predictReport?.heavyRoad

                when {
                    minorBuilding == "1.0" -> {
                        damageStatus = "Bangunan rusak ringan."
                    }
                    minorBridge == "1.0" -> {
                        damageStatus = "Jembatan rusak ringan."
                    }
                    minorRoad == "1.0" -> {
                        damageStatus = "Jalan rusak ringan."
                    }
                    moderateBuilding == "1.0" -> {
                        damageStatus = "Bagungan rusak sedang."
                    }
                    moderateBridge == "1.0" -> {
                        damageStatus = "Jembatan rusak sedang."
                    }
                    moderateRoad == "1.0" -> {
                        damageStatus = "Jalan rusak sedang."
                    }
                    heavyBuilding == "1.0" -> {
                        damageStatus = "Bangunan rusak berat."
                    }
                    heavyBridge == "1.0" -> {
                        damageStatus = "Jembatan rusak berat."
                    }
                    heavyRoad == "1.0" -> {
                        damageStatus = "Jalan rusak berat."
                    }
                }

                binding.tvPredict.text = damageStatus

//                updateStatus()
            }

            override fun onFailure(call: Call<PredictResponse>, t: Throwable) {
                Log.e("Post failed: ", t.message.toString())
            }

        })
    }

    private fun updateStatus() {
        val status = hashMapOf(
            "status" to damageStatus
        )

        firebaseFirestore.collection("reports").document(reportId).set(status)
            .addOnSuccessListener {
                binding.progressBar.visibility = View.GONE
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

    private fun selectImage() {
        val pickImage = Intent(Intent.ACTION_PICK)
        pickImage.type = "image/jpg"
        startActivityForResult(pickImage, 100)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 100 && resultCode == Activity.RESULT_OK && data != null) {
            imageUri = data?.data!!
            imgBitmap = MediaStore.Images.Media.getBitmap(activity?.contentResolver, imageUri)

            val bitmapDrawable = BitmapDrawable(imgBitmap)
            binding.imageView.setImageDrawable(bitmapDrawable)
            binding.btnPredict.visibility = View.VISIBLE
        }
    }

    private fun process() {

        val stream = ByteArrayOutputStream()
        imgBitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
        val streamImg = stream.toByteArray()
        imgString = Base64.encodeToString(streamImg, Base64.DEFAULT)

        predict()
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
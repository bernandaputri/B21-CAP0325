package com.arjuna.sipiapp.ui.profile

import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.arjuna.sipiapp.data.ReportEntity
import com.arjuna.sipiapp.databinding.FragmentProfileBinding
import com.google.firebase.firestore.FirebaseFirestore

class ProfileViewModel : ViewModel() {

    private val listReport = MutableLiveData<List<ReportEntity>>()

    fun getUserReport(
        username: String,
        binding: FragmentProfileBinding
    ) : MutableLiveData<List<ReportEntity>> {
        val firebaseFirestore = FirebaseFirestore.getInstance()
        firebaseFirestore.collection("reports").whereEqualTo("username", username).get()
            .addOnSuccessListener {
                if (it.isEmpty) {
                    binding.progressBar.visibility = View.GONE
                    binding.tvNoData.visibility = View.VISIBLE
                } else {
                    binding.progressBar.visibility = View.GONE
                    val userReport = it.map { report ->
                        ReportEntity (
                            report.getString("report_id").toString(),
                            report.getString("title").toString(),
                            report.getString("location").toString(),
                            report.getString("reported_by").toString(),
                            report.getString("imageUrl").toString(),
                            report.getString("status").toString(),
                            report.getString("filename").toString()
                        )
                    }
                    listReport.postValue(userReport)
                }
            }
        return listReport
    }

}
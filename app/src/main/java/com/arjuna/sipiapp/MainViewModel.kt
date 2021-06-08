package com.arjuna.sipiapp

import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.arjuna.sipiapp.data.ReportEntity
import com.arjuna.sipiapp.databinding.FragmentHomeBinding
import com.arjuna.sipiapp.databinding.FragmentProfileBinding
import com.google.firebase.firestore.FirebaseFirestore

class MainViewModel : ViewModel() {

    private val listReport = MutableLiveData<List<ReportEntity>>()

    fun getUserReport(
            username: String,
            binding: FragmentProfileBinding
    ) : LiveData<List<ReportEntity>> {
        val firebaseFirestore = FirebaseFirestore.getInstance()
        firebaseFirestore.collection("reports").whereEqualTo("reported_by", username).get()
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
                                report.getString("filename").toString(),
                            )
                        }.toList()
                        listReport.postValue(userReport)
                    }
                }
        return listReport
    }

    fun getAllReport(binding: FragmentHomeBinding) : LiveData<List<ReportEntity>> {
        val firebaseFirestore = FirebaseFirestore.getInstance()
        firebaseFirestore.collection("reports").get()
            .addOnSuccessListener {
                if (it.isEmpty) {
                    binding.homeProgressBar.visibility = View.GONE
                    binding.tvNoData.visibility = View.VISIBLE
                } else {
                    binding.homeProgressBar.visibility = View.GONE
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
                    }.toList()
                    listReport.postValue(userReport)
                }
                binding.homeProgressBar.visibility = View.GONE
            }
        return listReport
    }

}
package com.arjuna.sipiapp.ui.result

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.arjuna.sipiapp.data.ReportEntity
import com.arjuna.sipiapp.databinding.ActivityResultReportBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

class ResultReportActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_DATA = "extra_data"
    }

    private lateinit var binding: ActivityResultReportBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResultReportBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        setDataReport()
    }

    private fun setDataReport() {
        val report = intent.getParcelableExtra<ReportEntity>(EXTRA_DATA)
        Glide.with(this)
            .load(report?.reportImage)
            .apply(RequestOptions().override(250,250))
            .into(binding.imageView)
        binding.edtReportTitle.text = report?.reportName
        binding.edtLocation.text = report?.reportLocation
        binding.reportResult.text = report?.reportResult
    }
}
package com.arjuna.sipiapp.ui.home

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.arjuna.sipiapp.data.ReportEntity
import com.arjuna.sipiapp.databinding.ItemListBinding
import com.arjuna.sipiapp.ui.result.ResultReportActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions

class HomeAdapter : RecyclerView.Adapter<HomeAdapter.ListViewHolder>() {

    private var listReport = ArrayList<ReportEntity>()

    fun setReports(report: List<ReportEntity>) {
        this.listReport.clear()
        this.listReport.addAll(report)
        this.notifyDataSetChanged()
    }

    fun getReport(): ArrayList<ReportEntity> {
        return listReport
    }

    class ListViewHolder (private val binding: ItemListBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(reportEntity: ReportEntity) {
            with(binding) {
                Glide.with(itemView.context)
                    .load(reportEntity.reportImage)
                    .transform(RoundedCorners(15))
                    .apply(RequestOptions().override(60,60))
                    .into(reportImage)

                reportName.text = reportEntity.reportName
                reportSender.text = "Dilaporkan oleh ${reportEntity.reportUser}"
                reportStatus.text = reportEntity.reportResult
                reportEntity.reportId
                reportEntity.reportFilename
                reportEntity.reportLocation

                itemView.setOnClickListener {
                    val resultIntent = Intent(itemView.context, ResultReportActivity::class.java)
                    resultIntent.putExtra(ResultReportActivity.EXTRA_DATA, reportEntity)
                    itemView.context.startActivity(resultIntent)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val binding = ItemListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val report = listReport[position]
        holder.bind(report)
    }

    override fun getItemCount(): Int = listReport.size
}
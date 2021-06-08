package com.arjuna.sipiapp.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ReportEntity (
    var reportId: String,
    var reportName: String?,
    var reportLocation: String?,
    var reportUser: String?,
    var reportImage: String?,
    var reportResult: String?,
    var reportFilename: String?
) : Parcelable
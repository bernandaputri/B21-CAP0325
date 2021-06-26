package com.arjuna.sipiapp.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class UserModel(
    var email: String? = null,
    var username: String? = null,
    var name: String? = null
) : Parcelable
package com.arjuna.sipiapp.network

import com.arjuna.sipiapp.data.PredictResponse
import retrofit2.Call
import retrofit2.http.*

interface ApiService {

    @POST("predict?path={file_pic}")
    fun predictObject(
        @Path ("file_pic") file_pic: String
    ): Call<PredictResponse>

}
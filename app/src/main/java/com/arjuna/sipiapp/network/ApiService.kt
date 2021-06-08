package com.arjuna.sipiapp.network

import com.arjuna.sipiapp.data.PredictResponse
import retrofit2.Call
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiService {

    @POST("predict?path={file_name}.jpg")
    fun predictObject(
        @Path("file_name") file_name:String
    ): Call<PredictResponse>
}
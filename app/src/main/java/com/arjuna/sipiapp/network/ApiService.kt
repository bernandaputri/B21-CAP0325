package com.arjuna.sipiapp.network

import com.arjuna.sipiapp.data.PredictResponse
import retrofit2.Call
import retrofit2.http.*

interface ApiService {

    @Multipart
    @POST("predict?path={file_name}")
    fun predictObject(
        @Field("file_name") file_name:String
    ): Call<PredictResponse>

}
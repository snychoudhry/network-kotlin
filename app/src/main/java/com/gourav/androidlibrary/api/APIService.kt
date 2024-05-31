package com.gourav.androidlibrary.api

import com.google.gson.JsonElement
import com.gourav.retrofitlib.model.ResponseModel
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*

interface APIService {
    @GET("userProfile")
    fun getAppointmentDetails(
        @Query("email") email: String,
        @Query("password") password: String
    ): ResponseModel

//    @GET("top-headlines")
//    suspend fun getHeadlines(
//        @Query("country") country: String,
//        @Query("category") category: String,
//        @Query("apiKey") apiKey: String
//    ): Response<JsonElement>

    @GET("top-headlines")
    suspend fun getHeadlines(
        @QueryMap map:HashMap<String,String>
    ): Response<JsonElement>

    @Multipart
    @POST("top-headlines")
    suspend fun initUser(
        @Body requestBody: RequestBody,
        @Part userprofile:MultipartBody.Part
    ): Response<JsonElement>

    @GET("posts")
    suspend fun getPosts(): Response<JsonElement>

    @DELETE("/typicode/demo/posts/1")
    suspend fun deleteUser(): Response<JsonElement>
}
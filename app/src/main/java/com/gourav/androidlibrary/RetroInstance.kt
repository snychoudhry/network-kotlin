package com.gourav.androidlibrary

import com.gourav.retrofitlib.BuildRetrofit
import retrofit2.Retrofit

object RetroInstance {
    const val BASE_URL = "https://newsapi.org/v2/"
    const val BASE_URL2 = "https://jsonplaceholder.typicode.com"
    fun getRetroInstance(): Retrofit {
        return BuildRetrofit.getRetrofitInstance(BASE_URL)
    }
}
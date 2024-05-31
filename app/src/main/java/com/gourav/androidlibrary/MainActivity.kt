package com.gourav.androidlibrary

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import com.gourav.androidlibrary.api.APIService
import com.gourav.androidlibrary.models.PostModel
import com.gourav.retrofitlib.BuildRetrofit
import com.gourav.retrofitlib.model.ResponseModel
import com.gourav.uselibrary.models.NewsModel
import kotlinx.coroutines.*


class MainActivity : AppCompatActivity() {
    private val TAG: String? = "main>>>>"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val btn_click = findViewById<Button>(R.id.btn_click)
        btn_click.setOnClickListener {
            // Change BASE_URL is RetroInstance object as per the APIs below before using

            if(isNetworkAvailable(this))
            {
                getHeadlines() // URL = BASE_URL
//            getPosts() // URL = BASE_URL2
            }
            else{
                Toast.makeText(this,"Check your internet connection",Toast.LENGTH_SHORT).show()
            }
        }
    }

    /*response in JSONObject*/
    private fun getHeadlines() {
        /*start a coroutine scope*/
        CoroutineScope(Dispatchers.IO).launch {
            /*getting the retrofit instance from singleton and calling API*/
            try {

                val map= HashMap<String,String>()
                map["country"]= "us"
                map["category"] = "business"
                map["apiKey"] = "ba405e756b0a4141948317e8b122f777"
                val apiCall = RetroInstance.getRetroInstance()
                    .create(APIService::class.java)
                    .getHeadlines(map)
//                    .getHeadlines("us", "business", "ba405e756b0a4141948317e8b122f777")

                /*getting response in a variable*/
                val response: ResponseModel = BuildRetrofit.getResponse(apiCall)

                System.err.println(">>>>>>>>>> response code ${response.code}  ${response.message}")

                /*check response status if true or false*/
                if (response.status) {
                    /*if true - use convertResponse method to get response in your own data class object (here is NewsModel)*/
                    /*Note: here using **convertToObject** method to get data in NewsModel object format*/
                    val newsModel: NewsModel = BuildRetrofit.convertToObject(
                        response.Data.toString(),
                        NewsModel::class.java
                    )
                    Log.d(TAG, "onCreate: nm: ${newsModel.totalResults}")
                    Log.d(TAG, "onCreate: nm: ${newsModel.articles}")
                    runOnUiThread {
                        /*do UI related stuff*/
                    }
                }
                else {
                    if (response.code == 999){
                        runOnUiThread {
                            Toast.makeText(this@MainActivity," ${response.message} : Need to update the app.", Toast.LENGTH_SHORT).show()
                        }
                    }
                    else {
                        /*if false - Show error message as per response code*/
                        response.code
                        Log.e(TAG,
                            "onCreate: error message: ${response.code} : ${response.message}")
                    }
                }
            }
            catch (e:Exception){
                Log.e(TAG, "Error : ${e.localizedMessage}")
            }
        }
    }

    /*response in JSONArray*/
    private fun getPosts() {
        /*start a coroutine scope*/
        CoroutineScope(Dispatchers.IO).launch {
            /*getting the retrofit instance from singleton and calling API*/
            val apiCall = RetroInstance.getRetroInstance()
                .create(APIService::class.java)
                .getPosts()

            /*getting response in a variable*/
            val response = BuildRetrofit.getResponse(apiCall)

            /*check response status if true or false*/
            if (response.status) {
                /*if true - use convertResponse method to get response in your own data class object (here is PostModel)*/
                /*Note: here using **convertToList** method to get data in List<PostModel> format*/
                val dataList =
                    Gson().fromJson(response.Data.toString(), Array<PostModel>::class.java)
                        .toList()
                /*val postList =
                    BuildRetrofit.convertToList(
                        response.Data.toString(),
                        PostModel::class.java
                    )*/
                Log.d(TAG, "onCreate: size ab ${dataList.size}")
                Log.d(TAG, "onCreate: size ab $dataList")
                runOnUiThread {
                    /*do UI related stuff*/
                }
            }

            else {
                /*if false - Show error message as per response code*/
                response.code
                Log.e(TAG, "onCreate: error occured: ${response.message}")
            }

        }
    }

    private fun isNetworkAvailable(context: Context?): Boolean {
        if (context == null) return false
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val capabilities = connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
            if (capabilities != null) {
                when {
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> {
                        return true
                    }
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> {
                        return true
                    }
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> {
                        return true
                    }
                }
            }
        } else {
            val activeNetworkInfo = connectivityManager.activeNetworkInfo
            if (activeNetworkInfo != null && activeNetworkInfo.isConnected) {
                return true
            }
        }
        return false
    }
}
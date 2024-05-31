package com.gourav.retrofitlib.model

import com.google.gson.JsonElement

data class ResponseModel(
    val code: Int,
    val status: Boolean,
    val message: String,
    val Data: JsonElement?
)
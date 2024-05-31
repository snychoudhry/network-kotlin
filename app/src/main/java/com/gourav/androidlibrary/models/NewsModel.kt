package com.gourav.uselibrary.models

data class NewsModel(
    val articles: List<Article>,
    val status: String,
    val totalResults: Int
)
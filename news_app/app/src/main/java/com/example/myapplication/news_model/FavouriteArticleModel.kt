package com.example.myapplication.news_model

import com.google.gson.annotations.SerializedName

class FavouriteArticleModel(
    val title: String,
    val url: String,
    @SerializedName("urlToImage")
    val imageUrl: String?,
    val docId: String
)
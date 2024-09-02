package com.example.myapplication.models

import com.google.gson.annotations.SerializedName

class FavouriteArticleModel(
    val title: String,
    val url: String,
    val imageUrl: String?,
    val docId: String
)
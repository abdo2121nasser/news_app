package com.example.myapplication.call_interfaces

import com.example.myapplication.news_model.NewsModel
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Url

interface NewsCallable {

    @GET
    fun getArticles(@Url url: String): Call<NewsModel>

}
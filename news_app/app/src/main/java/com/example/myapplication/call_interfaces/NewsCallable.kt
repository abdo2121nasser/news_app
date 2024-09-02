package com.example.myapplication.call_interfaces

import com.example.myapplication.models.NewsModel
import retrofit2.Call
import retrofit2.http.GET

interface NewsCallable {

    @GET("/v2/everything?q=bitcoin&apiKey=823bf7004dde449bbd82ba85dfd69843")
    fun getArticles():Call<NewsModel>
}
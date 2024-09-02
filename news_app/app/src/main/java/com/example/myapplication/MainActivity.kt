package com.example.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.view.isVisible
import com.example.myapplication.adaptors.NewsAdaptor
import com.example.myapplication.call_interfaces.NewsCallable
import com.example.myapplication.databinding.ActivityMainBinding
import com.example.myapplication.models.NewsModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

//GET https://newsapi.org/v2/top-headlines?country=us&apiKey=823bf7004dde449bbd82ba85dfd69843
//end point /v2/top-headlines
//query country=us&apiKey=823bf7004dde449bbd82ba85dfd69843
// url https://newsapi.org
// api key
//823bf7004dde449bbd82ba85dfd69843
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        getData(binding)
        binding.refresher.setOnRefreshListener {
            getData(binding)
        }
    }

    fun getData(binding:ActivityMainBinding) {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://newsapi.org")
            .addConverterFactory(GsonConverterFactory.create())
            .build().create(NewsCallable::class.java)
        retrofit.getArticles().enqueue(object :Callback<NewsModel>{
            override fun onResponse(p0: Call<NewsModel>, response: Response<NewsModel>) {
           val news: NewsModel? = response.body()
                   showList(news!!, binding)
                binding.progress.isVisible=false
                binding.refresher.isRefreshing=false

            }

            override fun onFailure(p0: Call<NewsModel>, p1: Throwable) {
                binding.progress.isVisible=false

            }

        })
    }
    fun showList(newsModel: NewsModel,binding: ActivityMainBinding){
       val adaptor=NewsAdaptor(this,newsModel)
        binding.recycleList.adapter=adaptor

    }
}



package com.example.myapplication

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.view.menu.MenuBuilder
import androidx.core.view.isVisible
import com.example.myapplication.adaptors.NewsAdaptor
import com.example.myapplication.call_interfaces.NewsCallable
import com.example.myapplication.databinding.ActivityMainBinding
import com.example.myapplication.news_model.NewsModel
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

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.options_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId){
            R.id.favourites -> Toast.makeText(this,"Favourites Selected",Toast.LENGTH_SHORT).show()
            R.id.settings -> startActivity(Intent(this, SettingsActivity::class.java))
            R.id.logout -> Toast.makeText(this,"Logout Selected",Toast.LENGTH_SHORT).show()
        }
        return super.onOptionsItemSelected(item)
    }

    fun getData(binding:ActivityMainBinding) {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://newsapi.org")
            .addConverterFactory(GsonConverterFactory.create())
            .build().create(NewsCallable::class.java)
        //changing endpoint according to user preferences
        val reader = getSharedPreferences("userSettings", MODE_PRIVATE)
        val defaultEndpoint = "/v2/top-headlines?country=us&apiKey=abeb255c75614ac8a3eb8b01f3bd27ed"
        val endpoint = reader.getString("endpoint", defaultEndpoint)!!
        retrofit.getArticles(endpoint).enqueue(object :Callback<NewsModel>{
            override fun onResponse(p0: Call<NewsModel>, response: Response<NewsModel>) {
           val news: NewsModel? =response.body()
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



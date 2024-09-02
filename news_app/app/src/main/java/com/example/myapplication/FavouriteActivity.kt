package com.example.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.core.view.isVisible
import com.example.myapplication.adaptors.FavouriteAdaptor
import com.example.myapplication.databinding.ActivityFavourtiteBinding
import com.example.myapplication.news_model.FavouriteArticleModel
import com.example.myapplication.news_model.FavouriteModel
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore

class FavouriteActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityFavourtiteBinding.inflate(layoutInflater)
        setContentView(binding.root)
        getData(binding)
        binding.refresher.setOnRefreshListener {
            getData(binding)
        }
    }
    fun getData(binding: ActivityFavourtiteBinding) {
   val docId:String=getSharedPreferences("user_secrete_data", MODE_PRIVATE).getString("user_docId","").toString()
        val fireStore = Firebase.firestore
        fireStore.collection("users").document(docId).collection("favourites")
            .get().addOnSuccessListener {

                var articles = ArrayList<FavouriteArticleModel>()
                it.documents.map { document ->
                    articles.add(
                        FavouriteArticleModel(
                            title = document.getString("title") ?: "",
                            url = document.getString("url") ?: "",
                            imageUrl = document.getString("urlToImage"),
                            docId = document.id.toString()
                        )
                    )
                }
                showList(favouriteModel = FavouriteModel(articles = articles), binding = binding)
                binding.progress.isVisible = false
                binding.refresher.isRefreshing = false

            }
               .addOnFailureListener { exception ->
            Log.d("favourite",exception.toString())
                   binding.refresher.isRefreshing = false
        }
    }

    fun showList(favouriteModel: FavouriteModel, binding: ActivityFavourtiteBinding) {
        val adaptor = FavouriteAdaptor(this, favouriteModel)
        binding.favouriteList.adapter = adaptor

    }


}





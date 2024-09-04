package com.example.myapplication

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.myapplication.databinding.ActivityNewsSourceBinding

class NewsSourceActivity : AppCompatActivity() {
    private lateinit var binding: ActivityNewsSourceBinding
    private lateinit var sources: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityNewsSourceBinding.inflate(layoutInflater)

        setContentView(binding.root)

        getPreferences()

        binding.toolbar.setNavigationOnClickListener{
            startActivity(Intent(this, SettingsActivity::class.java))
            finish()
        }

        binding.toggleBtnGroup.addOnButtonCheckedListener { group, checkedId, _ ->
            when(checkedId){
                R.id.google_news_btn -> sources = "sources=google-news"
                R.id.cnn_btn -> sources = "sources=cnn"
                R.id.bbc_btn -> sources = "sources=bbc-news"
                R.id.fox_btn -> sources = "sources=fox-news"
                R.id.abc_btn -> sources = "sources=abc-news"
                else -> sources = "sources=msnbc"
            }
            savePreferences(group.checkedButtonId)
        }
    }

    private fun savePreferences(checkedBtn: Int) {
        val reader = getSharedPreferences("userSettings", Context.MODE_PRIVATE)
        val pageSize:Int = reader.getFloat("pageCount", 20F).toInt()
        val endpoint =
            "/v2/top-headlines?$sources&pageSize=$pageSize&apiKey=abeb255c75614ac8a3eb8b01f3bd27ed"
        val editor = getSharedPreferences("userSettings", Context.MODE_PRIVATE).edit()
        editor.putString("endpoint", endpoint)
        editor.putInt("sourcesBtn", checkedBtn)
        editor.putString("sources", sources)
        editor.apply()
    }

    private fun getPreferences(){
        val reader = getSharedPreferences("userSettings", Context.MODE_PRIVATE)
        binding.toggleBtnGroup.check(reader.getInt("sourcesBtn", R.id.google_news_btn))
    }
}
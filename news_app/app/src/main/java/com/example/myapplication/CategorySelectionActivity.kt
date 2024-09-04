package com.example.myapplication

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.annotation.IdRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.myapplication.databinding.ActivityCategorySelectionBinding
import com.google.android.material.button.MaterialButton

class CategorySelectionActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCategorySelectionBinding
    private lateinit var category:String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityCategorySelectionBinding.inflate(layoutInflater)

        setContentView(binding.root)

        getPreferences()

        binding.toolbar.setNavigationOnClickListener{
            startActivity(Intent(this, SettingsActivity::class.java))
            finish()
        }

        binding.toggleBtnGroup.addOnButtonCheckedListener { group, checkedId, _ ->
            when(checkedId){
                R.id.general_btn -> category = "&category=general"
                R.id.business_btn -> category = "&category=business"
                R.id.entertainment_btn -> category = "&category=entertainment"
                R.id.sports_btn -> category = "&category=sports"
                R.id.health_btn -> category = "&category=health"
                R.id.science_btn -> category = "&category=science"
                else -> category = "&category=technology"
            }
            savePreferences(group.checkedButtonId)
        }
    }

    private fun savePreferences(checkedBtn: Int) {
        val reader = getSharedPreferences("userSettings", Context.MODE_PRIVATE)
        val pageSize:Int = reader.getFloat("pageCount", 20F).toInt()
        val country = reader.getString("country", "country=us")
        val endpoint =
            "/v2/top-headlines?$country$category&pageSize=$pageSize&apiKey=abeb255c75614ac8a3eb8b01f3bd27ed"
        val editor = getSharedPreferences("userSettings", Context.MODE_PRIVATE).edit()
        editor.putInt("categoryBtn", checkedBtn)
        editor.putString("category", category)
        editor.putString("endpoint", endpoint)
        editor.apply()
    }

    private fun getPreferences(){
        val reader = getSharedPreferences("userSettings", Context.MODE_PRIVATE)
        binding.toggleBtnGroup.check(reader.getInt("categoryBtn", R.id.general_btn))
    }
}
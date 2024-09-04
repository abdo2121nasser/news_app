package com.example.myapplication

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.databinding.ActivitySettingsBinding

class SettingsActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySettingsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySettingsBinding.inflate(layoutInflater)

        setContentView(binding.root)

        getPreferences()

        val editor = getSharedPreferences("userSettings", Context.MODE_PRIVATE).edit()

        binding.checkbox.setOnCheckedChangeListener { _, isChecked ->
            when (isChecked) {
                true -> {
                    binding.srcBtn.isEnabled = true
                    binding.categoryBtn.isEnabled = false
                    binding.countryBtn.isEnabled = false
                    enableCustomSources()
                }

                false -> {
                    binding.srcBtn.isEnabled = false
                    binding.categoryBtn.isEnabled = true
                    binding.countryBtn.isEnabled = true
                    disableCustomSources()
                }
            }
            editor.putBoolean("isCustom", binding.checkbox.isChecked)
            editor.apply()
        }

        binding.slider.addOnChangeListener { _, value, _ ->
            editor.putFloat("pageCount", binding.slider.value)
            editor.apply()
            if(binding.checkbox.isChecked)
                enableCustomSources()
            else
                disableCustomSources()
        }

        binding.resetBtn.setOnClickListener {
            resetPreferences()
        }

        binding.categoryBtn.setOnClickListener {
            startActivity(Intent(this, CategorySelectionActivity::class.java))
            finish()
        }

        binding.countryBtn.setOnClickListener {
            startActivity(Intent(this, CountrySelectionActivity::class.java))
            finish()
        }

        binding.srcBtn.setOnClickListener {
            startActivity(Intent(this, NewsSourceActivity::class.java))
            finish()
        }
    }

    private fun enableCustomSources(){
        val reader = getSharedPreferences("userSettings", Context.MODE_PRIVATE)
        val pageSize:Int = reader.getFloat("pageCount", 20F).toInt()
        val sources = reader.getString("sources", "sources=google-news")
        val endpoint =
            "/v2/top-headlines?$sources&pageSize=$pageSize&apiKey=abeb255c75614ac8a3eb8b01f3bd27ed"
        val editor = getSharedPreferences("userSettings", Context.MODE_PRIVATE).edit()
        editor.putString("endpoint", endpoint)
        editor.apply()
    }

    private fun disableCustomSources(){
        val reader = getSharedPreferences("userSettings", Context.MODE_PRIVATE)
        val pageSize:Int = reader.getFloat("pageCount", 20F).toInt()
        val country = reader.getString("country", "country=us")
        val category = reader.getString("category", "&category=general")
        val endpoint =
            "/v2/top-headlines?$country$category&pageSize=$pageSize&apiKey=abeb255c75614ac8a3eb8b01f3bd27ed"
        val editor = getSharedPreferences("userSettings", Context.MODE_PRIVATE).edit()
        editor.putString("category", category)
        editor.putString("endpoint", endpoint)
        editor.apply()
    }


    private fun getPreferences(){
        val reader = getSharedPreferences("userSettings", Context.MODE_PRIVATE)
        binding.slider.value = reader.getFloat("pageCount", 20F)
        binding.checkbox.isChecked = reader.getBoolean("isCustom", false)
        if(binding.checkbox.isChecked){
            binding.srcBtn.isEnabled = true
            binding.categoryBtn.isEnabled = false
            binding.countryBtn.isEnabled = false
        }
    }

    private fun resetPreferences(){
        val editor = getSharedPreferences("userSettings", Context.MODE_PRIVATE).edit()
        editor.clear()
        editor.apply()
        binding.slider.value = 20F
        binding.checkbox.isChecked = false
        disableCustomSources()
    }

}
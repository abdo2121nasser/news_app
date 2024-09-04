package com.example.myapplication

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.myapplication.databinding.ActivityCountrySelectionBinding

class CountrySelectionActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCountrySelectionBinding
    private lateinit var country:String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityCountrySelectionBinding.inflate(layoutInflater)

        setContentView(binding.root)

        getPreferences()

        binding.toolbar.setNavigationOnClickListener{
            startActivity(Intent(this, SettingsActivity::class.java))
            finish()
        }

        binding.toggleBtnGroup.addOnButtonCheckedListener { group, checkedId, _ ->
            when(checkedId){
                R.id.eg_btn -> country = "country=eg"
                R.id.us_btn -> country = "country=us"
                R.id.uk_btn -> country = "country=uk"
                R.id.cn_btn -> country = "country=cn"
                R.id.ua_btn -> country = "country=ua"
                R.id.de_btn -> country = "country=de"
                else -> country = "country=fr"
            }
            savePreferences(group.checkedButtonId)
        }
    }

    private fun savePreferences(checkedBtn: Int) {
        val reader = getSharedPreferences("userSettings", Context.MODE_PRIVATE)
        val category = reader.getString("category", "&category=general")
        val pageSize:Int = reader.getFloat("pageCount", 20F).toInt()
        val endpoint =
            "/v2/top-headlines?$country$category&pageSize=$pageSize&apiKey=abeb255c75614ac8a3eb8b01f3bd27ed"
        val editor = getSharedPreferences("userSettings", Context.MODE_PRIVATE).edit()
        editor.putString("endpoint", endpoint)
        editor.putString("country", country)
        editor.putInt("countryBtn", checkedBtn)
        editor.apply()
    }

    private fun getPreferences(){
        val reader = getSharedPreferences("userSettings", Context.MODE_PRIVATE)
        binding.toggleBtnGroup.check(reader.getInt("countryBtn", R.id.us_btn))
    }

}
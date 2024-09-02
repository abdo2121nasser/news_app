package com.example.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.myapplication.databinding.ActivityFavourtiteBinding
import com.example.myapplication.databinding.ActivityProfileCreationBinding
import com.example.myapplication.models.ProfileModel
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore

class ProfileCreationActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityProfileCreationBinding.inflate(layoutInflater)
        setContentView(binding.root)
        createProfile(binding)
    }

    private fun createProfile(binding: ActivityProfileCreationBinding) {
        binding.createAccountButton.setOnClickListener {
            val email: String = binding.emailField.text.toString()
            val fireStore = Firebase.firestore
            fireStore.collection("users").add(ProfileModel(email)).addOnSuccessListener {
                Toast.makeText(this, "added user successfully", Toast.LENGTH_SHORT).show()
                getSharedPreferences("user_secrete_data", MODE_PRIVATE).edit()
                    .putString("user_docId", it.id.toString()).apply()
                    .toString()
            }.addOnFailureListener {
                Toast.makeText(this, it.toString(), Toast.LENGTH_SHORT).show()

            }
        }
    }


}
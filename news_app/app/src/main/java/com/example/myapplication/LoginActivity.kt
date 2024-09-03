package com.example.myapplication

import android.R.attr.password
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.myapplication.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityLoginBinding.inflate(layoutInflater)
        auth = Firebase.auth
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val sharedPreferences = getSharedPreferences("Login", Context.MODE_PRIVATE)
        val email = sharedPreferences.getString("email", "").toString()
        val password = sharedPreferences.getString("Pass", "").toString()
        if (email != "" && password != "")
            login(email,password)
        binding.signupTv.setOnClickListener {
            startActivity(Intent(this, SignUpActivity::class.java))
            finish()
        }
        binding.loginBtn.setOnClickListener {
            startActivity(Intent(this, SignUpActivity::class.java))
        }
        binding.loginBtn.setOnClickListener {
            val email = binding.emailEt.text.toString()
            val password = binding.passwordEt.text.toString()
            if (email.isBlank() || password.isBlank())
                Toast.makeText(this, "Missing Field/s!", Toast.LENGTH_SHORT).show()
            else {
                login(email, password)
            }

        }


        binding.forgetpassTv.setOnClickListener {
            startActivity(Intent(this, ResetPasswordActivity::class.java))
        }

    }

    private fun login(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    if (auth.currentUser!!.isEmailVerified) {
                        savePreferences(email, password)
                        startActivity(Intent(this, MainActivity::class.java))
                        finish()
                    } else {
                        Toast.makeText(this, "activate your email first", Toast.LENGTH_SHORT).show()
                    }

                } else {
                    Toast.makeText(this, "${task.exception?.message}", Toast.LENGTH_SHORT).show()
                }

            }
    }

    private fun savePreferences(email:String,password: String) {
        val sharedPreferences = getSharedPreferences("Login", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        if (binding.checkbox.isChecked) {

            editor.putString("email", email)
            editor.putString("Pass", password)
            editor.apply()
        }
//        else {
//            editor.putString("email", "")
//            editor.putString("Pass", "")
//            editor.apply()
//
//        }
    }

    private fun loadPreferences() {
        val sharedPreferences = getSharedPreferences("Login", Context.MODE_PRIVATE)
        val email = sharedPreferences.getString("email", null)
        val password = sharedPreferences.getString("Pass", null)
    }


}


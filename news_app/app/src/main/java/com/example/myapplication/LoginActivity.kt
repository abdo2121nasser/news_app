package com.example.myapplication

import android.R.attr.password
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
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
    private var temail =""
    private var tpassword=""
    private var tchecked=false

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
        //support get data after rotation
        temail = savedInstanceState?.getString("email")?:""
        binding.emailEt.setText(temail)
        tpassword = savedInstanceState?.getString("password")?:""
        binding.passwordEt.setText(tpassword)
        tchecked = savedInstanceState?.getBoolean("checked")?:true
        binding.checkbox.isChecked=tchecked

        //login automatically
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
            if (email.isBlank()&& password.isBlank()){
                binding.emailEt.error= "required!"
                binding.passwordEt.error= "required!"
            }
            else if (email.isBlank() )
                binding.emailEt.error= "required!"
            else if(password.isBlank())
            binding.passwordEt.error= "required!"

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

    }

    private fun loadPreferences() {
        val sharedPreferences = getSharedPreferences("Login", Context.MODE_PRIVATE)
        val email = sharedPreferences.getString("email", null)
        val password = sharedPreferences.getString("Pass", null)
    }
    override fun onSaveInstanceState(outState: Bundle, outPersistentState: PersistableBundle) {
        super.onSaveInstanceState(outState, outPersistentState)
        outState.putString("email", temail)
        outState.putString("password",tpassword)
        outState.putBoolean("checked",tchecked)
    }

    override fun onPause() {
        super.onPause()
        temail=binding.emailEt.text.toString()
        tpassword =binding.passwordEt.toString()
        tchecked= binding.checkbox.isChecked
    }



}


package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.widget.Toast
import android.window.OnBackInvokedDispatcher
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.myapplication.databinding.ActivitySignUpBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class SignUpActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignUpBinding
    private lateinit var auth: FirebaseAuth
    private var temail =""
    private var tpassword=""
    private var tcpassword=""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivitySignUpBinding.inflate(layoutInflater)
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
        tcpassword = savedInstanceState?.getString("cpassword")?:""
        binding.cpasswordEt.setText(tcpassword)

        auth = Firebase.auth
        binding.loginTv.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }

        binding.signupBtn.setOnClickListener {

            binding.signupBtn.setOnClickListener {
                val email = binding.emailEt.text.toString()
                val pass = binding.passwordEt.text.toString()
                val conPass = binding.cpasswordEt.text.toString()
                if (email.isBlank() || pass.isBlank() || conPass.isBlank())
                    Toast.makeText(this, "Missing Field/s!", Toast.LENGTH_SHORT).show()
                else if (pass.length < 6)
                    Toast.makeText(this, "Short Password!", Toast.LENGTH_SHORT).show()
                else if (pass != conPass)
                    binding.textInputCPassword.error = "Password not match!!"
                else {
                    signUpUser(email, pass)
                }
            }

        }
    }

    private fun signUpUser(email: String, pass: String) {
        auth.createUserWithEmailAndPassword(email, pass)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    verifyEmail()
                } else {
                    Toast.makeText(this, "${task.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun verifyEmail() {
        auth.currentUser!!.sendEmailVerification()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "check your email", Toast.LENGTH_SHORT).show()
                }
            }

    }
    override fun onSaveInstanceState(outState: Bundle, outPersistentState: PersistableBundle) {
        super.onSaveInstanceState(outState, outPersistentState)
        outState.putString("email", temail)
        outState.putString("password",tpassword)
        outState.putString("checked",tcpassword)
    }
    override fun onPause() {
        super.onPause()
        temail=binding.emailEt.text.toString()
        tpassword =binding.passwordEt.toString()
        tcpassword= binding.cpasswordEt.toString()
    }


}

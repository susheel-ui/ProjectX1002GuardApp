package com.example.project_b_security_gardapp

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.project_b_security_gardapp.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {
    lateinit var activityBinding: ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityBinding = ActivityLoginBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(activityBinding.root)

        activityBinding.buttonLogin.setOnClickListener {
           //TODO:: HERE WE WILL ADD THE ACTION ON LOGIN

            //AFTER WE WILL ADD THIS INTENT
                startActivity(Intent(this,HomeActivity::class.java))
        }
    }
}
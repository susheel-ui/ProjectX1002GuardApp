package com.example.project_b_security_gardapp

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.project_b_security_gardapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    lateinit var activityBinding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityBinding = ActivityMainBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(activityBinding.root)

        Handler().postDelayed({
          startActivity(Intent(this,LoginActivity::class.java))
//            Toast.makeText(this, "app is running", Toast.LENGTH_SHORT).show()
        },2000)
    }
}
package com.example.project_b_security_gardapp

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.project_b_security_gardapp.databinding.ActivityNewGuestFormBinding

class NewGuestFormActivity : AppCompatActivity() {
    lateinit var activityBinding: ActivityNewGuestFormBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityBinding = ActivityNewGuestFormBinding.inflate(layoutInflater)
        setContentView(activityBinding.root)
        enableEdgeToEdge()

    }
}
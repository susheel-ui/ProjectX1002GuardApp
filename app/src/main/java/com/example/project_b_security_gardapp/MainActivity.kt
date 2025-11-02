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

    }

    override fun onStart() {
        super.onStart()
        //code for automatic login from already login user
        Handler().postDelayed({
            val sharedPreferences = getSharedPreferences(Keywords.MYPREFS.toString(), MODE_PRIVATE)
            val token = sharedPreferences.getString(
                Keywords.USERTOKEN.toString(),
                Keywords.NOTFOUND.toString()
            )
            if (!token.equals(Keywords.NOTFOUND.toString())) {
                startActivity(Intent(this, HomeActivity::class.java))
            } else {
                startActivity(Intent(this, LoginActivity::class.java))
            }
//            Toast.makeText(this, "app is running", Toast.LENGTH_SHORT).show()
        }, 2000)
    }
}
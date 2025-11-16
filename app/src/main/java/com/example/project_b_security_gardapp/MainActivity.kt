package com.example.project_b_security_gardapp

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.project_b_security_gardapp.api.Entities.User
import com.example.project_b_security_gardapp.api.Repo.UserRepository
import com.example.project_b_security_gardapp.api.Retrofit.RetrofitInstance
import com.example.project_b_security_gardapp.api.Services.UserServices
import com.example.project_b_security_gardapp.databinding.ActivityMainBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response

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

// Login By Token and check valid or expired
                val service = RetrofitInstance.getInstance.create(UserServices::class.java)
                val repo  = UserRepository(service)
                var response: Response<User>? = null
                val job = CoroutineScope(Dispatchers.IO).launch {
                   response =  repo.GetUserBytoken(token.toString())
                }
                job.invokeOnCompletion {
                       if(response?.code() == 200){
                           startActivity(Intent(this, HomeActivity::class.java))
                       }else{
                           startActivity(Intent(this, LoginActivity::class.java))
                       }
                    }
            } else {
                startActivity(Intent(this, LoginActivity::class.java))
            }
//            Toast.makeText(this, "app is running", Toast.LENGTH_SHORT).show()
        }, 2000)
    }
}
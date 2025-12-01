package com.example.project_b_security_gardapp

import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.project_b_security_gardapp.api.Entities.userLoginEntity
import com.example.project_b_security_gardapp.api.Repo.UserRepository
import com.example.project_b_security_gardapp.api.Retrofit.RetrofitInstance
import com.example.project_b_security_gardapp.api.Services.UserServices
import com.example.project_b_security_gardapp.databinding.ActivityLoginBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LoginActivity : AppCompatActivity() {
    lateinit var activityBinding: ActivityLoginBinding

    //    lateinit var loginViewModel: loginViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityBinding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(activityBinding.root) // Set content view first

        val userServices = RetrofitInstance.getInstance.create(UserServices::class.java)
        val userRepository = UserRepository(userServices)
//        loginViewModel = ViewModelProvider(this, loginViewModelFactory(userRepository))[loginViewModel::class.java]

        enableEdgeToEdge()

        activityBinding.RegisterAccountTV.setOnClickListener{
            startActivity(Intent(applicationContext, GardRegistrationActivity::class.java))
        }

        activityBinding.buttonLogin.setOnClickListener {
//           loginViewModel.login() // Call the login function from the ViewModel

            val loading = AlertDialog.Builder(this).setView(R.layout.loading_layout).create()
            loading.show()

            if (activityBinding.mobileNumber.text.toString().isEmpty()) {
                activityBinding.mobileNumber.error = "Please enter your phone number"
                loading.dismiss()

            }else if (activityBinding.password.text.toString().isEmpty()) {
                activityBinding.password.error = "Please enter your password"
                loading.dismiss()
            } else {
                val phoneNumber = activityBinding.mobileNumber.text.toString()
                val password = activityBinding.password.text.toString()

                val job = CoroutineScope(Dispatchers.IO).launch {
                    try {
                        val loginEntity = userLoginEntity()
                        loginEntity.phoneNumber = "+91".plus(phoneNumber)
                        loginEntity.password = password
                        val result = userRepository.login(loginEntity)
                        Log.d(TAG, "onCreate: ${result.code()}")
                        Log.d(TAG, "onCreate result : ${result.body()?.token}")
                        if (result.code() == 200) {
                            CoroutineScope(Dispatchers.IO).launch {
                                if (result.body() != null) {
                                    val token = result.body()?.token
                                    val sharedPreferences = getSharedPreferences(
                                        Keywords.GUARD_MY_PREFS.toString(),
                                        Context.MODE_PRIVATE
                                    )
                                    val editor = sharedPreferences.edit()
                                    editor.putString(Keywords.GUARD_USER_TOKEN.toString(), token)
                                    editor.putString(Keywords.GUARD_OwnerId.toString(),result.body()!!.userId.toString())
                                    editor.apply()
                                    loading.dismiss()
                                    withContext(Dispatchers.IO){
                                        startActivity(Intent(this@LoginActivity, HomeActivity::class.java))
                                        finish()
                                    }
                                }
                            }.invokeOnCompletion {
//                                val intent = Intent(applicationContext, WebSocketService::class.java)

//                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//                                    startForegroundService(intent)
//                                } else {
//                                    startService(intent)
//                                }
//                                startActivity(
//                                    Intent(
//                                        applicationContext,
//                                        HomeActivity::class.java
//                                    )
//                                )
                            }
                        } else {
                            if(result.code() == 403){
                                withContext(Dispatchers.Main){
                                    loading.dismiss()
                                    Toast.makeText(applicationContext,"Invalid Credentials",Toast.LENGTH_SHORT).show()
                                }
                            }
                            if (result.code() == 500) {
                                try {
                                    withContext(Dispatchers.Main) {
                                        loading.dismiss()
                                        Toast.makeText(
                                            applicationContext,
                                            "*** Invalid MobileNumber Or Password ****",
                                            Toast.LENGTH_LONG
                                        ).show()
                                        //TODO:: here we have to make textview visible that will give a error
                                    }
                                } catch (e: Exception) {
                                    Log.d(TAG, "onCreate: error in coroutine running")
                                }
                            }else{

                            }
                        }
                    } catch (e: Exception) {
                        Log.d(TAG, "onCreate exceptions: ${e.message}")
                    }
                }
            }

        }
    }
}
package com.example.project_b_security_gardapp

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.project_b_security_gardapp.api.Entities.SignUpUserEntity
import com.example.project_b_security_gardapp.api.Entities.User
import com.example.project_b_security_gardapp.api.Repo.UserRepository
import com.example.project_b_security_gardapp.api.Retrofit.RetrofitInstance
import com.example.project_b_security_gardapp.api.Services.UserServices
import com.example.project_b_security_gardapp.databinding.ActivityGardRegistrationBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class GardRegistrationActivity : AppCompatActivity() {
    lateinit var binding: ActivityGardRegistrationBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGardRegistrationBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)

        binding.buttonSignUp.setOnClickListener {
            val loadingDialog = AlertDialog.Builder(this)
                .setView(R.layout.fragment_loading)
                .setCancelable(false)
                .create()

            if (validateInputs()) {
                loadingDialog.show()
                // Proceed with registration
                //TODO:: here we have to do work for registration Guard End
                val name = binding.EtName.text.toString().trim()
                val password = binding.EtPassword.text.toString().trim()
                val confirmPassword = binding.EtConfirmPassword.text.toString().trim()
                val mobile = "+91"+binding.EtMobile.text.toString().trim()
                val society = binding.EtSocietyName.text.toString().trim()

               val user = SignUpUserEntity(fullName = name, password = password, phoneNumber = mobile, societyCode = society)
                val services = RetrofitInstance.getInstance.create(UserServices::class.java)
                val repo = UserRepository(services)
               CoroutineScope(Dispatchers.IO).launch {
                   val response = repo.SignUp(user)
                   if(response.isSuccessful && response.code() == 200){
                       loadingDialog.dismiss()
                       finish()
                   }else{
                       loadingDialog.dismiss()
                   }
               }


            }
        }
        binding.loginPageText.setOnClickListener {
            finish()
        }

    }
    private fun validateInputs(): Boolean {
        val name = binding.EtName.text.toString().trim()
        val password = binding.EtPassword.text.toString().trim()
        val confirmPassword = binding.EtConfirmPassword.text.toString().trim()
        val mobile = "+91"+binding.EtMobile.text.toString().trim()
        val society = binding.EtSocietyName.text.toString().trim()

        when {
            name.isEmpty() -> {
                binding.EtName.error = "Please enter your name"
                binding.EtName.requestFocus()
                return false
            }
            password.isEmpty() -> {
                binding.EtPassword.error = "Please enter a password"
                binding.EtPassword.requestFocus()
                return false
            }
            password.length < 6 -> {
                binding.EtPassword.error = "Password must be at least 6 characters"
                binding.EtPassword.requestFocus()
                return false
            }
            confirmPassword != password -> {
                binding.EtConfirmPassword.error = "Passwords do not match"
                binding.EtConfirmPassword.requestFocus()
                return false
            }
            mobile.isEmpty() -> {
                binding.EtMobile.error = "Enter mobile number"
                binding.EtMobile.requestFocus()
                return false
            }
//            mobile.length != 10 -> {
//                binding.EtMobile.error = "Invalid mobile number"
//                binding.EtMobile.requestFocus()
//                return false
//            }
            society.isEmpty() -> {
                binding.EtSocietyName.error = "Enter society name"
                binding.EtSocietyName.requestFocus()
                return false
            }
            else -> {

                return true
            }
        }
    }

}
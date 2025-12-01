package com.example.project_b_security_gardapp

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.project_b_security_gardapp.Fragments.Attendance_Fragment
import com.example.project_b_security_gardapp.Fragments.HistoryFragment
import com.example.project_b_security_gardapp.Fragments.HomeFragment
import com.example.project_b_security_gardapp.Fragments.UserFragment
import com.example.project_b_security_gardapp.api.Repo.UserRepository
import com.example.project_b_security_gardapp.api.Retrofit.RetrofitInstance
import com.example.project_b_security_gardapp.api.Services.UserServices
import com.example.project_b_security_gardapp.databinding.ActivityHomeBinding
import com.example.project_b_security_gardapp.viewModels.HomeActivityViewModel.HomeViewModel
import com.example.project_b_security_gardapp.viewModels.HomeActivityViewModel.HomeViewModelFactory

class HomeActivity : AppCompatActivity() {
    lateinit var activityBinding: ActivityHomeBinding
    lateinit var viewModel: HomeViewModel
    lateinit var userServices: UserServices
    lateinit var userRepository: UserRepository
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityBinding = ActivityHomeBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(activityBinding.root)

        val sharedPreferences = getSharedPreferences(Keywords.GUARD_MY_PREFS.toString(), MODE_PRIVATE)
        val token = sharedPreferences.getString(Keywords.GUARD_USER_TOKEN.toString(),"NotFound")

        if(token.toString().equals("NotFound")){
                    Toast.makeText(this, token.toString(), Toast.LENGTH_SHORT).show()
        }

        try {
             userServices = RetrofitInstance.getInstance.create(UserServices::class.java)
            userRepository = UserRepository(userServices)
            viewModel = ViewModelProvider(this,HomeViewModelFactory(userRepository)).get(HomeViewModel::class.java)
        } catch (e: Exception) {
            Log.d(TAG, "onCreate: ${e.message}")
        }

        changeFragment(HomeFragment())
        activityBinding.bottomNavigationBar.setOnItemSelectedListener {
            when(it.itemId){
                R.id.btm_nav_home -> changeFragment(HomeFragment())
                R.id.btm_nav_history -> changeFragment(HistoryFragment())
                R.id.btm_nav_User -> changeFragment(UserFragment())
                R.id.btn_Attendance -> changeFragment(Attendance_Fragment())
                else -> {
                    true
                }
            }
        }
        activityBinding.FabNewGuestForm.setOnClickListener {
            startActivity(Intent(this,NewGuestFormActivity::class.java))
        }
    }

    override fun onStart() {
        super.onStart()

    }
    private fun changeFragment(fragment : Fragment): Boolean {
        val fragmentManager  = supportFragmentManager
        try {
            fragmentManager.beginTransaction()
                .replace(activityBinding.fragmentContainerView.id,fragment)
                .commit()
            return true
        } catch (e: Exception) {
            return false
        }
    }
}
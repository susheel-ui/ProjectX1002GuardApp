package com.example.project_b_security_gardapp

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import com.example.project_b_security_gardapp.Fragments.HistoryFragment
import com.example.project_b_security_gardapp.Fragments.HomeFragment
import com.example.project_b_security_gardapp.Fragments.UserFragment
import com.example.project_b_security_gardapp.databinding.ActivityHomeBinding

class HomeActivity : AppCompatActivity() {
    lateinit var activityBinding: ActivityHomeBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        activityBinding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(activityBinding.root)
        changeFragment(HomeFragment())
        activityBinding.bottomNavigation.setOnItemSelectedListener {
            when(it.itemId){
                R.id.btm_nav_home -> changeFragment(HomeFragment())
                R.id.btm_nav_history -> changeFragment(HistoryFragment())
                R.id.btm_nav_User -> changeFragment(UserFragment())
                else -> {
                    true
                }
            }
        }
        activityBinding.FabNewGuestForm.setOnClickListener {
            startActivity(Intent(this,NewGuestFormActivity::class.java))
        }
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
package com.example.project_b_security_gardapp

import android.content.ContentValues.TAG
import android.content.SharedPreferences
import android.content.res.ColorStateList
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.LifecycleObserver
import com.example.project_b_security_gardapp.Services.WebSocketHelper
import com.example.project_b_security_gardapp.databinding.ActivityVeiwRequestBinding
import com.example.project_b_security_gardapp.viewModels.ViewRequestViewModel

class VeiwRequestActivity : AppCompatActivity() {
    lateinit var binding: ActivityVeiwRequestBinding
    lateinit var requestViewModel: ViewRequestViewModel
    lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVeiwRequestBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        sharedPreferences = getSharedPreferences(Keywords.GUARD_MY_PREFS.toString(), MODE_PRIVATE)
        val token =
            sharedPreferences.getString(Keywords.GUARD_USER_TOKEN.toString(), Keywords.GUARD_NOT_FOUND.toString())
        requestViewModel = ViewRequestViewModel(token.toString())
        val id = intent.getStringExtra("id")
        Log.d(TAG, "onCreate id = : $id")
        Log.d("Data", "onCreate: $token")
        if (id != null) {
            requestViewModel.getRequestById(id, token.toString())

        }

        requestViewModel.request.observe(this) { visitor ->
            binding.tvRequestName.text = visitor.guestName
            binding.tvMobileNumber.text = visitor.phoneNumber
            binding.tvDescription.text = visitor.description
            binding.tvSociety.text = visitor.societyName
            binding.tvStatus.text = visitor.status
            binding.tvOwnerName.text = visitor.ownerName
            val color = when (visitor.status) {
                "PENDING" -> ContextCompat.getColor(this, R.color.orange_yellow)   // orange/yellow
                "REJECTED" -> ContextCompat.getColor(this, R.color.red) // red
                else -> ContextCompat.getColor(this, R.color.green)       // green
            }
            binding.tvStatus.backgroundTintList = ColorStateList.valueOf(color)

            //btn and status handling
            // ---------- STATUS HANDLING ----------
            if (visitor.status == "PENDING") {
                binding.btnIn.visibility = View.GONE
                binding.btnOut.visibility = View.GONE
            } else if (visitor.status == "REJECTED") {

                binding.btnIn.visibility = View.GONE
                binding.btnOut.visibility = View.GONE
                binding.tvStatus.backgroundTintList = ColorStateList.valueOf(color)
            } else {

                binding.tvStatus.backgroundTintList = ColorStateList.valueOf(color)
                // Always reset both first
                binding.btnIn.visibility = View.GONE
                binding.btnOut.visibility = View.GONE

                if (visitor.checkInTime == null) {

                    // Not checked in yet
                    binding.btnIn.visibility = View.VISIBLE

                } else if (visitor.checkOutTime == null) {

                    // Checked in but not checked out
                    binding.btnOut.visibility = View.VISIBLE

                } else {

                    // Both exist → hide both
                    binding.btnIn.visibility = View.GONE
                    binding.btnOut.visibility = View.GONE
                }
            }



//            if (visitor.status == "PENDING" || visitor.status == "REJECTED") {
//
//                binding.btnIn.visibility = View.GONE
//                binding.btnOut.visibility = View.GONE
//                binding.tvStatus.setBackgroundColor(
//                    ContextCompat.getColor(this, R.color.red)
//                )
//
//            } else {
//
//                binding.tvStatus.setBackgroundColor(
//                    ContextCompat.getColor(this, R.color.green)
//                )
//
//                // Always reset both first
//                binding.btnIn.visibility = View.GONE
//                binding.btnOut.visibility = View.GONE
//
//                if (visitor.checkInTime == null) {
//                    // Not checked in yet
//                    binding.btnIn.visibility = View.VISIBLE
//
//                } else if (visitor.checkOutTime == null) {
//                    // Checked in but not checked out
//                    binding.btnOut.visibility = View.VISIBLE
//
//                } else {
//                    // Already checked in & checked out → hide both
//                    binding.btnIn.visibility = View.GONE
//                    binding.btnOut.visibility = View.GONE
//                }
//            }

        }



        // Here we have to call functionality of checkIn
        binding.btnIn.setOnClickListener {
            requestViewModel.RequestCheckIn(id=id.toString(),token = token.toString())
            finish()
        }
        // Here we have to call functionality of  CheckOut
        binding.btnOut.setOnClickListener {
            requestViewModel.requestCheckOut(id=id.toString(),token = token.toString())
            finish()
        }


        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val loadingDialog = AlertDialog.Builder(this)
            .setView(R.layout.fragment_loading)
            .setCancelable(false)
            .create()

        requestViewModel.loading.observe(this) {

            if (it) {
                Log.d("Loading Indicator", "onStart: loading $it ")
                loadingDialog.show()
            }else{
                loadingDialog.dismiss()
            }
        }

    }

    override fun onStart() {
        super.onStart()

    }
}
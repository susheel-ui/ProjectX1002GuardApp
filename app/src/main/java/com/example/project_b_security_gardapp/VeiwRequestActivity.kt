package com.example.project_b_security_gardapp

import android.content.ContentValues.TAG
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
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
        sharedPreferences = getSharedPreferences(Keywords.MYPREFS.toString(), MODE_PRIVATE)
        val token =
            sharedPreferences.getString(Keywords.USERTOKEN.toString(), Keywords.NOTFOUND.toString())
        requestViewModel = ViewRequestViewModel(token.toString())
        val id = intent.getStringExtra("id")
        Log.d(TAG, "onCreate id = : $id")
        if (id != null) {
            requestViewModel.getRequestById(id, token.toString())
            Log.d("Data", "onCreate: $token")
        }

        requestViewModel.request.observe(this) { visitor ->
            binding.tvRequestName.text = visitor.guestName
            binding.tvMobileNumber.text = visitor.phoneNumber
            binding.tvDescription.text = visitor.description
            binding.tvSociety.text = visitor.societyName
            binding.tvStatus.text = visitor.status
            binding.tvOwnerName.text = visitor.ownerName

            //btn and status handling
            if(visitor.status.equals("PENDING")){
                binding.btnIn.visibility = View.GONE
                binding.btnOut.visibility = View.GONE
                binding.tvStatus.setBackgroundColor(getColor(R.color.red))
            }else{
                binding.tvStatus.setBackgroundColor(getColor(R.color.green))
                binding.btnOut.visibility = View.VISIBLE
                binding.btnIn.visibility = View.VISIBLE
            }





        }



        // Here we have to call functionality of checkIn
        binding.btnIn.setOnClickListener {
            requestViewModel.RequestCheckIn(id=id.toString(),token = token.toString())
        }
        // Here we have to call functionality of  CheckOut
        binding.btnOut.setOnClickListener {
            requestViewModel.requestCheckOut(id=id.toString(),token = token.toString())
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

            loadingDialog.create().apply {
                window?.setBackgroundDrawableResource(android.R.color.transparent)
            }

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
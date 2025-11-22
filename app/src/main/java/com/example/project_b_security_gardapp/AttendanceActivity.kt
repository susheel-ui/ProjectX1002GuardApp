package com.example.project_b_security_gardapp

import android.content.ContentValues.TAG
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.project_b_security_gardapp.Adapters.TodayAttandanceListAdapter
import com.example.project_b_security_gardapp.databinding.ActivityAttendanceBinding
import com.example.project_b_security_gardapp.viewModels.StaffAttendanceViewModel


class AttendanceActivity : AppCompatActivity() {

    lateinit var binding: ActivityAttendanceBinding
    lateinit var viewModel: StaffAttendanceViewModel
    lateinit var sharedPreferences: SharedPreferences
    lateinit var adapter: TodayAttandanceListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityAttendanceBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val staffcode = intent.getStringExtra("staff_code")
        sharedPreferences = getSharedPreferences(Keywords.GUARD_MY_PREFS.toString(), MODE_PRIVATE)
        val token = sharedPreferences.getString(Keywords.GUARD_USER_TOKEN.toString(),Keywords.GUARD_NOT_FOUND.toString())

        viewModel = StaffAttendanceViewModel()

        binding.rcTodayAttendence.layoutManager = LinearLayoutManager(this)
        adapter = TodayAttandanceListAdapter(this, emptyList(),viewModel,token.toString())
        binding.rcTodayAttendence.adapter = adapter

        viewModel.todayStaffLoading.observe(this){

        }
        viewModel.todayStaffAttendanceList.observe(this){
            Log.d(TAG, "onCreate: $it")
            adapter = TodayAttandanceListAdapter(this,it,viewModel,token.toString())
            binding.rcTodayAttendence.adapter = adapter
        }
        if(staffcode != null){
            Log.d(TAG, "onCreate: $staffcode")
            viewModel.startAttendanceOfStaff(staffcode.toString(),token.toString())
           if(!token.equals(Keywords.GUARD_NOT_FOUND.toString())){
               viewModel.getTodayAttendanceStartedStaff(token.toString())
           }else{
               Log.d(TAG, "onCreate: token is not comming here $token")
           }
        }else{
            Log.d(TAG, "onCreate: Direct comes")
            viewModel.getTodayAttendanceStartedStaff(token.toString())
        }
    }
}
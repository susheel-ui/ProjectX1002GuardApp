package com.example.project_b_security_gardapp.viewModels

import android.content.ContentValues.TAG
import android.util.Log
import android.widget.MultiAutoCompleteTextView
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.project_b_security_gardapp.api.Entities.User
import com.example.project_b_security_gardapp.api.Repo.UserRepository
import com.example.project_b_security_gardapp.api.Responses.ResponseStaffArrayItem
import com.example.project_b_security_gardapp.api.Responses.TodayStaffEntity
import com.example.project_b_security_gardapp.api.Retrofit.RetrofitInstance
import com.example.project_b_security_gardapp.api.Services.UserServices
import kotlinx.coroutines.launch
import java.security.Provider.Service

class StaffAttendanceViewModel():ViewModel() {
    lateinit var repo:UserRepository

    private val _staffAttendanceList = MutableLiveData<List<ResponseStaffArrayItem>>()
    val staffAttendanceList: LiveData<List<ResponseStaffArrayItem>> = _staffAttendanceList

    private val _todayStaffAttendanceList = MutableLiveData<List<TodayStaffEntity>>()
    val todayStaffAttendanceList: LiveData<List<TodayStaffEntity>> = _todayStaffAttendanceList

    //loading
    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = _loading

    private val _todayStaffLoading =  MutableLiveData<Boolean>()
    val todayStaffLoading:LiveData<Boolean> = _todayStaffLoading


    init {
        val servce = RetrofitInstance.getInstance.create(UserServices::class.java)
        repo = UserRepository(servce)
    }

    fun getStaffs(token:String){
        _loading.postValue(true)
        viewModelScope.launch {
            val result = repo.getStaff("Bearer ".plus(token))
            if(result.isSuccessful && result.code() == 200){
                _staffAttendanceList.postValue(result.body())
                Log.d("Success", "getStaffs: ${result.body()} ")
                _loading.postValue(false)
            }else{
                Log.d("Error", "getStaffs: ${result.code()}")
                _loading.postValue(false)

            }

        }
    }
    fun startAttendanceOfStaff(staffCode:String,token: String){
        viewModelScope.launch {
               val result =  repo.startAttendanceOfStaff(staffCode.toInt(),"Bearer ".plus(token))
            if (result.isSuccessful && result.code() == 200){
                Log.d("API result Success", "startAttendanceOfStaff: ${result.code()} ${result.message()}")
            }else{
                Log.d("API result failed", "startAttendanceOfStaff: ${result.code()}}")
            }
        }

    }
    fun getTodayAttendanceStartedStaff(token: String){
            _todayStaffLoading.postValue(true)
            viewModelScope.launch {
                val result = repo.getAllTodayStaffAttendance("Bearer ".plus(token))
                if(result.isSuccessful && result.code() == 200){
                    Log.d("Success", "getTodayAttendanceStartedStaff: ${result.code()}")
                    Log.d("Success", "getTodayAttendanceStartedStaff: ${result.body()}")
                    _todayStaffAttendanceList.postValue(result.body())
                    _todayStaffLoading.postValue(false)
                }else{
                    _todayStaffLoading.postValue(false)
                    Log.d("Error", "getTodayAttendanceStartedStaff: ${result.code()}")
                }
            }
    }

    fun serchingAttendanceOfStaff(id:String){

    }
    fun serachingTodayAttendanceStartedStaff(id:String){

    }
    fun checkIn(id:Int,token: String){
        viewModelScope.launch {
           val result =  repo.staffCheckIn(id,"Bearer ".plus(token))
            if(result.isSuccessful && result.code() == 200){
                getTodayAttendanceStartedStaff(token)
                Log.d(TAG, "checkIn: suceess full ")
            }else{
                Log.d(TAG, "checkIn: not CheckIn servier Problem")
            }
        }


    }
    fun checkOut(id:Int,token: String){
        viewModelScope.launch {
            val result =  repo.staffCheckOut(id,"Bearer ".plus(token))
            if(result.isSuccessful && result.code() == 200){
                getTodayAttendanceStartedStaff(token)
                Log.d(TAG, "checkOut: suceess full ")
            }else{
                Log.d(TAG, "checkIn: not checkOut servier Problem")
            }
        }

    }
}
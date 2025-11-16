package com.example.project_b_security_gardapp.viewModels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.project_b_security_gardapp.api.Entities.RequestsResultEntity
import com.example.project_b_security_gardapp.api.Repo.UserRepository
import com.example.project_b_security_gardapp.api.Retrofit.RetrofitInstance
import com.example.project_b_security_gardapp.api.Services.UserServices
import kotlinx.coroutines.launch

class ViewRequestViewModel(val requestId:String):ViewModel(){
    private val repo: UserRepository

    // 🔹 LiveData to hold the guest request list
    private val _request = MutableLiveData<RequestsResultEntity>()
      val request:LiveData<RequestsResultEntity> = _request

    // 🔹 LiveData for loading state
    private val _loading = MutableLiveData<Boolean>(true)
    val loading: LiveData<Boolean> = _loading


    init {
        val apiInstance = RetrofitInstance.getInstance
        val userService = apiInstance.create(UserServices::class.java)
        repo = UserRepository(userService)
    }

    fun getRequestById(id:String,token:String){
            viewModelScope.launch {
              try {
                 val result =  repo.getRequestById(id,"Bearer $token")
                  if(result.isSuccessful && result.code() == 200){
                      Log.d("Success", "getRequestById: ${result.body()}")
                      _request.postValue(result.body())
                      _loading.postValue(false)
                  }else{
                      Log.d("Error", "getRequestById: ${result.code()}")
                  }
              }catch (e:Exception){
                  Log.d("Exception", "getRequestById: ${e.toString()} ")
              }
            }
    }

    fun RequestCheckIn(id:String,token:String){
        viewModelScope.launch {
            val result = repo.VisitorCheckIn(token = "Bearer $token", id = id)
            if(result.isSuccessful && result.code() == 200){
                Log.d("Success", "RequestCheckIn: ${result.body()}")
            }else{
                Log.d("Error", "RequestCheckIn: ${result.code()}")
            }
        }
    }
    fun requestCheckOut(id: String,token: String){
        viewModelScope.launch {
            val result = repo.VisitorCheckOut(token = "Bearer $token", id = id)
            if(result.isSuccessful && result.code() == 200){
                Log.d("Success", "RequestCheckIn: ${result.body()}")
            }else{
                Log.d("Error", "RequestCheckIn: ${result.code()}")
            }
        }
    }

}
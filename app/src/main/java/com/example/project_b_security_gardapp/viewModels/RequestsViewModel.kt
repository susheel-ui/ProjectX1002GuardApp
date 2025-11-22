package com.example.project_b_security_gardapp.viewModels

import android.content.ContentValues.TAG
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

class RequestsViewModel : ViewModel() {

    private val repo: UserRepository

    // 🔹 LiveData to hold the guest request list
    private val _requestsLiveData = MutableLiveData<List<RequestsResultEntity>>()
    val requestsLiveData: LiveData<List<RequestsResultEntity>> = _requestsLiveData

    // 🔹 LiveData to hold the guest request list
    private val _recentRequestsLiveData = MutableLiveData<List<RequestsResultEntity>>()
    val recentRequestsLiveData: LiveData<List<RequestsResultEntity>> = _recentRequestsLiveData
    // 🔹 Loading indicator LiveData
    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = _loading

    // 🔹 Error message LiveData
    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage

    init {
        val apiInstance = RetrofitInstance.getInstance
        val userService = apiInstance.create(UserServices::class.java)
        repo = UserRepository(userService)
    }

    /**
     * Fetch guest requests list from API and post it to LiveData
     */
    fun getGuestRequests(token: String) {
        viewModelScope.launch {
            _loading.postValue(true)
            try {
                val response = repo.getAllGuestRequests("Bearer $token")

                if (response.isSuccessful && response.body() != null) {
                    val result = response.body()!!
                    // ✅ "data" contains the actual list of guest requests
                    _requestsLiveData.postValue(result)
                    if (result.size > 3) {
                        _recentRequestsLiveData.postValue(result.subList(0,3))
                    }else{
                        _recentRequestsLiveData.postValue(result.subList(0,result.size))
                    }
                    Log.d("RequestsViewModel", "Fetched ${result.size} guest requests")
                } else {
                    val msg = "Failed: ${response.code()} - ${response.message()}"
                    _errorMessage.postValue(msg)
                    Log.e("RequestsViewModel", msg)
                }

            } catch (e: Exception) {
                _errorMessage.postValue(e.localizedMessage ?: "Unexpected error")
                Log.e("RequestsViewModel", "Exception: ${e.message}", e)
            } finally {
                _loading.postValue(false)
            }
        }
    }
}

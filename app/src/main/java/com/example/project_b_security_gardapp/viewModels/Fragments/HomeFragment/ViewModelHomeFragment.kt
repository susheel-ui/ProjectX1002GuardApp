package com.example.project_b_security_gardapp.viewModels.Fragments.HomeFragment

import android.content.ContentValues.TAG
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.project_b_security_gardapp.api.Entities.Entity_HomePageRequests
import com.example.project_b_security_gardapp.api.Entities.User
import com.example.project_b_security_gardapp.api.Repo.UserRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class ViewModelHomeFragment(private val userRepository: UserRepository) : ViewModel() {

    private val _userData = MutableLiveData<User>()
    val userData: LiveData<User> = _userData

    private val _recentRequects = MutableLiveData<List<Entity_HomePageRequests>>()
    val recentRequests: LiveData<List<Entity_HomePageRequests>> = _recentRequects


    val loading = MutableLiveData<Boolean>()
    val errorMessage = MutableLiveData<String>()

    private var isRunning = true

    fun stopLoop() {
        isRunning = false
    }

    private fun startRepeatingApiCall(token:String) {
        viewModelScope.launch {
            while (isRunning) {
                // API call
                val result = userRepository.getHomePageRequests("Bearer $token")
                if (result.isSuccessful && result.body() != null) {
                    Log.d(TAG, "startRepeatingApiCall: ${result.body()}")
                    _recentRequects.postValue(result.body())
                }
                delay(5000)
            }
        }
    }

    init {
        Thread{
            Handler(Looper.getMainLooper()).postDelayed({
                loading.postValue(false)
            }, 3000)
        }
    }

    fun getUserInfo(token: String) {
        startRepeatingApiCall(token)
        viewModelScope.launch {
            loading.postValue(true)
            try {
                val result = userRepository.GetUserBytoken(token)
                if (result.isSuccessful && result.body() != null) {
                    _userData.postValue(result.body())
                } else {
                    errorMessage.postValue("Failed: ${result.code()} - ${result.message()}")
                    Log.e(TAG, "Failed: ${result.code()} - ${result.message()}")
                }
            } catch (e: Exception) {
                errorMessage.postValue(e.localizedMessage ?: "Unexpected error")
                Log.e(TAG, "Error fetching user info: ${e.message}")
            } finally {
                loading.postValue(false)
            }
        }

    }

}

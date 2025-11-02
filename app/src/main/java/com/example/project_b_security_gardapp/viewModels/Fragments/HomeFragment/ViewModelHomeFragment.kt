package com.example.project_b_security_gardapp.viewModels.Fragments.HomeFragment

import android.content.ContentValues.TAG
import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.project_b_security_gardapp.api.Entities.User
import com.example.project_b_security_gardapp.api.Repo.UserRepository
import kotlinx.coroutines.launch

class ViewModelHomeFragment(private val userRepository: UserRepository) :ViewModel() {
    private val UserData = MutableLiveData<User>()
    val data:LiveData<User> = UserData


    fun getUserInfo(token:String){
        viewModelScope.launch {
           val result =  userRepository.GetUserBytoken(token)
           if (result.body()!= null){
               UserData.postValue(result.body())
           }
        }

    }
}
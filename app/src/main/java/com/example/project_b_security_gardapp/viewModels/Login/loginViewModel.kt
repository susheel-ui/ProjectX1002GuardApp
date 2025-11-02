package com.example.project_b_security_gardapp.viewModels.Login

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.project_b_security_gardapp.api.Repo.UserRepository
import com.example.project_b_security_gardapp.api.Responses.UserLoginResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class loginViewModel(private val repository: UserRepository): ViewModel(){
        fun login(){
//           viewModelScope.launch(Dispatchers.IO){
//               repository.login()
//           }
        }
    val livedata: LiveData<UserLoginResponse>
        get() = repository.loginLiveData
}
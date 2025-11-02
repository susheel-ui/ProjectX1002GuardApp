package com.example.project_b_security_gardapp.viewModels.Login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.project_b_security_gardapp.api.Repo.UserRepository

class loginViewModelFactory(private  val repository: UserRepository) : ViewModelProvider.Factory{

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return loginViewModel(repository) as T
    }

}
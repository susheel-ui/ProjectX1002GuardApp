package com.example.project_b_security_gardapp.viewModels.HomeActivityViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.project_b_security_gardapp.api.Repo.UserRepository

@Suppress("UNCHECKED_CAST")
class HomeViewModelFactory(private val userRepository: UserRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return HomeViewModel(userRepository) as T
    }
}
package com.example.project_b_security_gardapp.viewModels.Fragments.HomeFragment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.project_b_security_gardapp.api.Repo.UserRepository

class HomeViewModelFactory(private val userRepository: UserRepository):ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ViewModelHomeFragment(userRepository) as T
    }
}
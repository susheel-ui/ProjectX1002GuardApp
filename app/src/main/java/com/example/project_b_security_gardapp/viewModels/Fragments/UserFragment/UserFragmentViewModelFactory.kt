package com.example.project_b_security_gardapp.viewModels.Fragments.UserFragment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.project_b_security_gardapp.api.Repo.UserRepository

class UserFragmentViewModelFactory(private val userRepository: UserRepository) :ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ViewModelUserFragment(userRepository) as T
    }
}
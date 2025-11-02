package com.example.project_b_security_gardapp.viewModels.Fragments.UserFragment

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.project_b_security_gardapp.api.Entities.User
import com.example.project_b_security_gardapp.api.Repo.UserRepository
import kotlinx.coroutines.launch

class ViewModelUserFragment(private val userRepository: UserRepository) :ViewModel() {
    val user = MutableLiveData<User>()
    val data:LiveData<User> = user

    fun getData(token :String){
        viewModelScope.launch {
            val result = userRepository.GetUserBytoken(token)
            result.let {
                user.postValue(result.body())
            }
        }
    }
}
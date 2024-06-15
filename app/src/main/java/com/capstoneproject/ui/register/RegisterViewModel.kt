package com.capstoneproject.ui.register

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.capstoneproject.data.Resource
import com.capstoneproject.data.model.register.RegisterRequest
import com.capstoneproject.data.model.register.RegisterResponse
import com.capstoneproject.data.repository.UserRepository

class RegisterViewModel: ViewModel() {
    private val userRepository = UserRepository()

    private val _registerResponse = MutableLiveData<Resource<RegisterResponse>>()
    val registerResponse: LiveData<Resource<RegisterResponse>> get() = _registerResponse

    fun registerUser(registerRequest: RegisterRequest) {
        _registerResponse.value = Resource.Loading()
        userRepository.registerUser(registerRequest) { response ->
            _registerResponse.value = response
        }
    }
}
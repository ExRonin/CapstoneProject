package com.capstoneproject.ui.login

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.capstoneproject.data.Resource
import com.capstoneproject.data.model.login.LoginRequest
import com.capstoneproject.data.model.login.LoginResponse
import com.capstoneproject.data.repository.UserRepository

class LoginViewModel(application: Application) : AndroidViewModel(application) {
    private val userRepository = UserRepository()
    private val sharedPreferences: SharedPreferences =
        application.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)

    private val _loginResponse = MutableLiveData<Resource<LoginResponse>>()
    val loginResponse: LiveData<Resource<LoginResponse>> get() = _loginResponse

    fun loginUser (loginRequest: LoginRequest) {
        _loginResponse.value = Resource.Loading()
        userRepository.loginUser(loginRequest) { response ->
            _loginResponse.value = response
        }
    }

    fun saveDataToken(idUser: String, token: String, refreshToken: String) {
        val editor = sharedPreferences.edit()
        editor.putString("idUser", idUser)
        editor.putString("token", token)
        editor.putString("refreshToken", refreshToken)
        editor.putLong("timestamp", System.currentTimeMillis())
        editor.apply()
    }
}
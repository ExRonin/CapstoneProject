package com.capstoneproject.ui.profile

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.capstoneproject.data.Resource
import com.capstoneproject.data.model.logout.LogoutResponse
import com.capstoneproject.data.model.user.DetailUserResponse
import com.capstoneproject.data.repository.UserRepository

class ProfileViewModel(application: Application): AndroidViewModel(application) {

    private val userRepository = UserRepository()
    private val sharedPreferences: SharedPreferences =
        application.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)

    private val _logoutResponse = MutableLiveData<Resource<LogoutResponse>>()
    val logoutResponse: LiveData<Resource<LogoutResponse>> get() = _logoutResponse
    private val _detailUserResponse = MutableLiveData<Resource<DetailUserResponse>>()
    val detailUserResponse: LiveData<Resource<DetailUserResponse>> get() = _detailUserResponse

    private val _token = MutableLiveData<String>()
    val token: LiveData<String> get() = _token
    private val _refreshToken = MutableLiveData<String>()
    val refreshToken: LiveData<String> get() = _refreshToken
    private val _idUser = MutableLiveData<String>()
    val idUser: LiveData<String> get() = _idUser

    fun logoutUser(token: String) {
        _logoutResponse.value = Resource.Loading()
        userRepository.logoutUser(token) { response ->
            _logoutResponse.value = response
        }
    }

    fun getDetailUser(idUser: String, token: String) {
        _detailUserResponse.value = Resource.Loading()
        userRepository.getDetailUser(idUser = idUser, token = token) { response ->
            _detailUserResponse.value = response
        }
    }

    fun getDataToken() {
        val token = sharedPreferences.getString("token", null)
        val refreshToken = sharedPreferences.getString("refreshToken", null)
        val idUser = sharedPreferences.getString("idUser", null)
        _token.value = token
        _refreshToken.value = refreshToken
        _idUser.value = idUser
    }

    fun clearDataToken() {
        val editor = sharedPreferences.edit()
        editor.remove("idUser")
        editor.remove("token")
        editor.remove("refreshToken")
        editor.apply()
        _token.value = null
        _refreshToken.value = null
        _idUser.value = null
    }

}
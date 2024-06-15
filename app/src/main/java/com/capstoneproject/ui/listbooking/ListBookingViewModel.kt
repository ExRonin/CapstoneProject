package com.capstoneproject.ui.listbooking

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.capstoneproject.data.Resource
import com.capstoneproject.data.model.order.OrderResponse
import com.capstoneproject.data.repository.OrderRepository

class ListBookingViewModel(application: Application): AndroidViewModel(application) {
    private val orderRepository = OrderRepository()
    private val sharedPreferences: SharedPreferences =
        application.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)

    private val _orderByUserIdResponse = MutableLiveData<Resource<OrderResponse>>()
    val orderByUserIdResponse: LiveData<Resource<OrderResponse>> get() = _orderByUserIdResponse

    private val _token = MutableLiveData<String>()
    val token: LiveData<String> get() = _token
    private val _refreshToken = MutableLiveData<String>()
    val refreshToken: LiveData<String> get() = _refreshToken
    private val _idUser = MutableLiveData<String>()
    val idUser: LiveData<String> get() = _idUser

    fun getDataToken() {
        val token = sharedPreferences.getString("token", null)
        val refreshToken = sharedPreferences.getString("refreshToken", null)
        val idUser = sharedPreferences.getString("idUser", null)
        _token.value = token
        _refreshToken.value = refreshToken
        _idUser.value = idUser
    }

    fun getOrdersByUserId(id:String, token:String) {
        _orderByUserIdResponse.value = Resource.Loading()
        orderRepository.getOrdersByIdUser(id, token) { response ->
            _orderByUserIdResponse.value = response
        }
    }
}
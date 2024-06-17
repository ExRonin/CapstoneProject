package com.capstoneproject.ui.bookingprocess.bookingdetail

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.capstoneproject.data.Resource
import com.capstoneproject.data.model.order.CreateOrderRequest
import com.capstoneproject.data.model.order.CreateOrderResponse
import com.capstoneproject.data.model.user.DetailUserResponse
import com.capstoneproject.data.repository.OrderRepository
import com.capstoneproject.data.repository.UserRepository

class BookingDetailViewModel(application: Application): AndroidViewModel(application) {

    private val userRepository = UserRepository()
    private val orderRepository = OrderRepository()
    private val sharedPreferences: SharedPreferences =
        application.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)

    private val _detailUserResponse = MutableLiveData<Resource<DetailUserResponse>>()
    val detailUserResponse: LiveData<Resource<DetailUserResponse>> get() = _detailUserResponse

    private val _createOrderResponse = MutableLiveData<Resource<CreateOrderResponse>>()
    val createOrderResponse: LiveData<Resource<CreateOrderResponse>> get() = _createOrderResponse

    private val _token = MutableLiveData<String>()
    val token: LiveData<String> get() = _token
    private val _refreshToken = MutableLiveData<String>()
    val refreshToken: LiveData<String> get() = _refreshToken
    private val _idUser = MutableLiveData<String>()
    val idUser: LiveData<String> get() = _idUser

    fun getDetailUser(idUser: String, token: String) {
        _detailUserResponse.value = Resource.Loading()
        userRepository.getDetailUser(idUser = idUser, token = token) { response ->
            _detailUserResponse.value = response
        }
    }

    fun createOrder(token: String, createOrderRequest: CreateOrderRequest) {
        _createOrderResponse.value = Resource.Loading()
        orderRepository.createOrder(token, createOrderRequest) { response ->
            _createOrderResponse.value = response
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

}
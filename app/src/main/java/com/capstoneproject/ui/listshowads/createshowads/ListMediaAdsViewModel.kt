package com.capstoneproject.ui.listshowads.createshowads

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.capstoneproject.data.Resource
import com.capstoneproject.data.model.order.OrderResponse
import com.capstoneproject.data.repository.OrderRepository

class ListMediaAdsViewModel(application: Application): AndroidViewModel(application) {
    private val orderRepository = OrderRepository()

    private val _orderByUserIdResponse = MutableLiveData<Resource<OrderResponse>>()
    val orderByUserIdResponse: LiveData<Resource<OrderResponse>> get() = _orderByUserIdResponse

    fun getOrdersByUserId(id:String, token:String) {
        _orderByUserIdResponse.value = Resource.Loading()
        orderRepository.getOrdersByIdUser(id, token) { response ->
            _orderByUserIdResponse.value = response
        }
    }
}
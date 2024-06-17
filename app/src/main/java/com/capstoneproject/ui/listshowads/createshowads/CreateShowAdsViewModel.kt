package com.capstoneproject.ui.listshowads.createshowads

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.capstoneproject.data.Resource
import com.capstoneproject.data.model.order.CreateShowAdsRequest
import com.capstoneproject.data.model.order.CreateShowAdsResponse
import com.capstoneproject.data.repository.OrderRepository

class CreateShowAdsViewModel(application: Application): AndroidViewModel(application) {
    private val orderRepository = OrderRepository()

    private val _createShowAdsResponse = MutableLiveData<Resource<CreateShowAdsResponse>>()
    val createShowAdsResponse: LiveData<Resource<CreateShowAdsResponse>> get() = _createShowAdsResponse

    fun createShowAds(token: String, createAdsRequest: CreateShowAdsRequest) {
        _createShowAdsResponse.value = Resource.Loading()
        orderRepository.createShowAds(token, createAdsRequest){ response ->
            _createShowAdsResponse.value = response
        }
    }
}
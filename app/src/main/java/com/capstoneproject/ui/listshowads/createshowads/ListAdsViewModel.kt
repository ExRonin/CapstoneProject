package com.capstoneproject.ui.listshowads.createshowads

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.capstoneproject.data.Resource
import com.capstoneproject.data.model.advertisingcontent.AdvertisingContentsResponse
import com.capstoneproject.data.repository.AdvertisingContentRepository

class ListAdsViewModel(application: Application): AndroidViewModel(application) {

    private val advertisingContentRepository = AdvertisingContentRepository()

    private val _advertisingContentsByUserIdResponse = MutableLiveData<Resource<AdvertisingContentsResponse>>()
    val advertisingContentsByUserIdResponse: LiveData<Resource<AdvertisingContentsResponse>> get() = _advertisingContentsByUserIdResponse

    fun getAdvertisingContentsByUserId(id:String, token:String) {
        _advertisingContentsByUserIdResponse.value = Resource.Loading()
        advertisingContentRepository.getAdvertisingContentsByUserId(id, token) { response ->
            _advertisingContentsByUserIdResponse.value = response
        }
    }
}
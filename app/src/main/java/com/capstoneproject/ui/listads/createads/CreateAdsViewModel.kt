package com.capstoneproject.ui.listads.createads

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.capstoneproject.data.Resource
import com.capstoneproject.data.model.advertisingcontent.CreateAdvertisingContentResponse
import com.capstoneproject.data.repository.AdsRepository
import java.io.File

class CreateAdsViewModel(application: Application) : AndroidViewModel(application) {

    private val adsRepository = AdsRepository()

    private val _createAdsResponse = MutableLiveData<Resource<CreateAdvertisingContentResponse>>()
    val createAdsResponse: LiveData<Resource<CreateAdvertisingContentResponse>> get() = _createAdsResponse

    fun createAds(
        token: String,
        userId: String,
        name: String,
        category: String,
        image: File,
    ) {
        _createAdsResponse.value = Resource.Loading()
        adsRepository.createAds(
            token,
            userId,
            name,
            category,
            image
        ) { response ->
            _createAdsResponse.value = response
        }
    }
}
package com.capstoneproject.ui.listads

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.capstoneproject.data.Resource
import com.capstoneproject.data.model.advertisingcontent.AdvertisingContentsResponse
import com.capstoneproject.data.repository.AdvertisingContentRepository

class ListAdsViewModel(application: Application): AndroidViewModel(application) {
    private val advertisingContentRepository = AdvertisingContentRepository()
    private val sharedPreferences: SharedPreferences =
        application.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)

    private val _advertisingContentsByUserIdResponse = MutableLiveData<Resource<AdvertisingContentsResponse>>()
    val advertisingContentsByUserIdResponse: LiveData<Resource<AdvertisingContentsResponse>> get() = _advertisingContentsByUserIdResponse

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

    fun getAdvertisingContentsByUserId(id:String, token:String) {
        _advertisingContentsByUserIdResponse.value = Resource.Loading()
        advertisingContentRepository.getAdvertisingContentsByUserId(id, token) { response ->
            _advertisingContentsByUserIdResponse.value = response
        }
    }

}
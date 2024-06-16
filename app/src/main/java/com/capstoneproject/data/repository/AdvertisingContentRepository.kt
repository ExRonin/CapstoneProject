package com.capstoneproject.data.repository

import com.capstoneproject.data.Resource
import com.capstoneproject.data.model.advertisingcontent.AdvertisingContentsResponse
import com.capstoneproject.data.source.remote.network.RetrofitInstance
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AdvertisingContentRepository {

    private val apiService = RetrofitInstance.api

    fun getAdvertisingContentsByUserId(id: String, token: String, callback: (Resource<AdvertisingContentsResponse>) -> Unit) {
        apiService.getAdvertisingContentsByIdUser(id, "Bearer ${token}").enqueue(object: Callback<AdvertisingContentsResponse> {
            override fun onResponse(call: Call<AdvertisingContentsResponse>, response: Response<AdvertisingContentsResponse>) {
                if (response.isSuccessful) {
                    callback(Resource.Success(response.body()))
                } else {
                    val errorBody = response.errorBody()?.string()
                    try {
                        val json = errorBody?.let { JSONObject(it) }
                        val errorMessage = json?.getString("message")
                        callback(Resource.Error(errorMessage))

                    } catch (e: JSONException) {
                        callback(Resource.Error(e.message))
                    }
                }
            }

            override fun onFailure(call: Call<AdvertisingContentsResponse>, t: Throwable) {
                callback(Resource.Error(t.message))
            }
        })
    }


}
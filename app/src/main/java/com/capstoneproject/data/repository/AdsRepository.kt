package com.capstoneproject.data.repository

import com.capstoneproject.data.Resource
import com.capstoneproject.data.model.advertisingcontent.CreateAdvertisingContentResponse
import com.capstoneproject.data.source.remote.network.RetrofitInstance
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class AdsRepository {

    private val apiService = RetrofitInstance.api

    fun createAds(
        token: String,
        userId: String,
        name: String,
        category: String,
        image: File,
        callback: (Resource<CreateAdvertisingContentResponse>) -> Unit
    ) {
        val userIdRequestBody = RequestBody.create("text/plain".toMediaTypeOrNull(), userId)
        val nameRequestBody = RequestBody.create("text/plain".toMediaTypeOrNull(), name)
        val categoryRequestBody = RequestBody.create("text/plain".toMediaTypeOrNull(), category)

        val requestFile = RequestBody.create("image/*".toMediaTypeOrNull(), image)
        val imagePart = MultipartBody.Part.createFormData("image", image.name, requestFile)

        apiService.createAds(
            token = token,
            userId = userIdRequestBody,
            name = nameRequestBody,
            category = categoryRequestBody,
            image = imagePart
        ).enqueue(object : Callback<CreateAdvertisingContentResponse> {
            override fun onResponse(
                call: Call<CreateAdvertisingContentResponse>,
                response: Response<CreateAdvertisingContentResponse>
            ) {
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

            override fun onFailure(
                call: Call<CreateAdvertisingContentResponse>,
                t: Throwable
            ) {
                callback(Resource.Error(t.message))
            }
        })
    }
}
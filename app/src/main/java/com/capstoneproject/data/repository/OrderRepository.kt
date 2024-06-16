package com.capstoneproject.data.repository

import com.capstoneproject.data.Resource
import com.capstoneproject.data.model.login.LoginResponse
import com.capstoneproject.data.model.order.OrderResponse
import com.capstoneproject.data.source.remote.network.RetrofitInstance
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class OrderRepository {

    private val apiService = RetrofitInstance.api

    fun getOrdersByIdUser(id: String, token: String, callback: (Resource<OrderResponse>) -> Unit) {
        apiService.getOrdersByIdUser(id, "Bearer ${token}").enqueue(object: Callback<OrderResponse> {
            override fun onResponse(call: Call<OrderResponse>, response: Response<OrderResponse>) {
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

            override fun onFailure(call: Call<OrderResponse>, t: Throwable) {
                callback(Resource.Error(t.message))
            }
        })
    }

    fun getOrdersShowAdvertisementByIdUser(id:String, token: String, callback: (Resource<OrderResponse>) -> Unit) {
        apiService.getOrdersShowAdvertisementByIdUser(id, "Bearer ${token}").enqueue(object: Callback<OrderResponse> {
            override fun onResponse(call: Call<OrderResponse>, response: Response<OrderResponse>) {
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

            override fun onFailure(call: Call<OrderResponse>, t: Throwable) {
                callback(Resource.Error(t.message))
            }
        })
    }
}
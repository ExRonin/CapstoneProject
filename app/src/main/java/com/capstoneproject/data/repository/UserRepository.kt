package com.capstoneproject.data.repository

import com.capstoneproject.data.Resource
import com.capstoneproject.data.model.login.LoginRequest
import com.capstoneproject.data.model.login.LoginResponse
import com.capstoneproject.data.source.remote.network.RetrofitInstance
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserRepository {

    private val apiService = RetrofitInstance.api

    fun loginUser(loginRequest: LoginRequest, callback: (Resource<LoginResponse>) -> Unit) {
        apiService.login(loginRequest).enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                if (response.isSuccessful) {
                    callback(Resource.Success(response.body()))
                } else {
                    val errorBody = response.errorBody()?.string()
                    try {
                        val json = JSONObject(errorBody)
                        val errorMessage = json.getString("message")
                        callback(Resource.Error(errorMessage))

                    } catch (e: JSONException) {
                        callback(Resource.Error(e.message))
                    }
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                callback(Resource.Error(t.message))
            }
        })

    }
}
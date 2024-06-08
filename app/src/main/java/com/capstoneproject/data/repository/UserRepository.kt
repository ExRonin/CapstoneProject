package com.capstoneproject.data.repository

import com.capstoneproject.data.Resource
import com.capstoneproject.data.model.login.LoginRequest
import com.capstoneproject.data.model.login.LoginResponse
import com.capstoneproject.data.model.logout.LogoutResponse
import com.capstoneproject.data.model.register.RegisterRequest
import com.capstoneproject.data.model.register.RegisterResponse
import com.capstoneproject.data.model.user.DetailUserResponse
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

    fun registerUser(
        registerRequest: RegisterRequest,
        callback: (Resource<RegisterResponse>) -> Unit
    ) {
        apiService.register(registerRequest).enqueue(object : Callback<RegisterResponse> {
            override fun onResponse(
                call: Call<RegisterResponse>,
                response: Response<RegisterResponse>
            ) {
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

            override fun onFailure(p0: Call<RegisterResponse>, t: Throwable) {
                callback(Resource.Error(t.message))
            }
        })
    }

    fun logoutUser(token: String, callback: (Resource<LogoutResponse>) -> Unit) {
        apiService.logout(token = "Bearer $token").enqueue(object : Callback<LogoutResponse> {
            override fun onResponse(call: Call<LogoutResponse>, response: Response<LogoutResponse>) {
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

            override fun onFailure(call: Call<LogoutResponse>, t: Throwable) {
                callback(Resource.Error(t.message))
            }
        })
    }

    fun getDetailUser(idUser: String, token: String, callback: (Resource<DetailUserResponse>) -> Unit) {
        apiService.getDetailUser(id = idUser, token = "Bearer $token").enqueue(object : Callback<DetailUserResponse> {
            override fun onResponse(
                call: Call<DetailUserResponse>,
                response: Response<DetailUserResponse>
            ) {
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

            override fun onFailure(call: Call<DetailUserResponse>, t: Throwable) {
                callback(Resource.Error(t.message))
            }
        })
    }
}
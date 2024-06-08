package com.capstoneproject.data.source.remote.network


import com.capstoneproject.data.model.login.LoginRequest
import com.capstoneproject.data.model.login.LoginResponse
import com.capstoneproject.data.model.register.RegisterRequest
import com.capstoneproject.data.model.register.RegisterResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {
    @POST("auth/login")
    fun login(@Body request: LoginRequest): Call<LoginResponse>

    @POST("auth/register")
    fun register(@Body request: RegisterRequest): Call<RegisterResponse>

}




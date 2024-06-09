package com.capstoneproject.data.source.remote.network


import com.capstoneproject.data.model.login.LoginRequest
import com.capstoneproject.data.model.login.LoginResponse
import com.capstoneproject.data.model.logout.LogoutResponse
import com.capstoneproject.data.model.register.RegisterRequest
import com.capstoneproject.data.model.register.RegisterResponse
import com.capstoneproject.data.model.user.DetailUserResponse
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiService {
    @POST("auth/login")
    fun login(@Body request: LoginRequest): Call<LoginResponse>

    @POST("auth/register")
    fun register(@Body request: RegisterRequest): Call<RegisterResponse>

    @POST("auth/logout")
    fun logout(
        @Header("Authorization") token: String,
    ): Call<LogoutResponse>

    @GET("users/{id}")
    fun getDetailUser(
        @Path("id") id: String,
        @Header("Authorization") token: String,
    ): Call<DetailUserResponse>

    @PATCH("users/survey/{id}")
    suspend fun updateSurveyStatus(
        @Header("Authorization") token: String,
        @Path("id") userId: String,
        @Body request: UpdateSurveyStatusRequest
    ): Response<UpdateSurveyStatusResponse>
}

data class UpdateSurveyStatusRequest(
    val userId: String,
    val isFillSurvey: Boolean
)

data class UpdateSurveyStatusResponse(
    val status: Boolean,
    val message: String
)






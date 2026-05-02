package com.example.hr_app.data.api

import com.example.hr_app.data.dto.requests.ChangePasswordRequest
import com.example.hr_app.data.dto.requests.LoginRequest
import com.example.hr_app.data.dto.requests.RegisterRequest
import com.example.hr_app.data.dto.responses.AuthResponse
import com.example.hr_app.data.dto.responses.UserResponse
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST

interface AuthApi {
    @POST("auth/register")
    suspend fun register(@Body request: RegisterRequest): AuthResponse

    @POST("auth/login")
    suspend fun login(@Body request: LoginRequest): AuthResponse

    @GET("auth/me")
    suspend fun getCurrentUser(): UserResponse

    @POST("auth/change-password")
    suspend fun changePassword(@Body request: ChangePasswordRequest)

    @DELETE("auth/delete-account")
    suspend fun deleteAccount()
}

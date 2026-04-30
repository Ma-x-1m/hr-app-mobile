package com.example.hr_app.data.dto.requests

import kotlinx.serialization.Serializable

@Serializable
data class LoginRequest(
    val email: String,
    val password: String
)

@Serializable
data class RegisterRequest(
    val email: String,
    val password: String,
    val name: String,
    val role: String
)

@Serializable
data class ChangePasswordRequest(
    val oldPassword: String,
    val newPassword: String
)

package com.example.hr_app.domain.models

data class User(
    val id: String,
    val email: String,
    val name: String,
    val role: UserRole,
    val createdAt: String
)

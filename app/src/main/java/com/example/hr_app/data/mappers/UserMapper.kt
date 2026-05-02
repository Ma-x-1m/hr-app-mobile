package com.example.hr_app.data.mappers

import com.example.hr_app.data.dto.responses.UserResponse
import com.example.hr_app.domain.models.User
import com.example.hr_app.domain.models.UserRole

fun UserResponse.toDomain(): User = User(
    id = id,
    email = email,
    name = name,
    role = UserRole.fromString(role),
    createdAt = createdAt
)

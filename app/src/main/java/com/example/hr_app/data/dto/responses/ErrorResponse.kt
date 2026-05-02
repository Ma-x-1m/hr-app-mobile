package com.example.hr_app.data.dto.responses

import kotlinx.serialization.Serializable

@Serializable
data class ErrorResponse(
    val error: String
)

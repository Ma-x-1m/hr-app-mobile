package com.example.hr_app.data.dto.requests

import kotlinx.serialization.Serializable

@Serializable
data class SendMessageRequest(
    val content: String
)

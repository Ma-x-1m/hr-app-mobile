package com.example.hr_app.data.dto.responses

import kotlinx.serialization.Serializable

@Serializable
data class ConversationResponse(
    val id: String,
    val applicationId: String,
    val createdAt: String
)

@Serializable
data class MessageResponse(
    val id: String,
    val conversationId: String,
    val senderId: String,
    val content: String,
    val sentAt: String,
    val isRead: Boolean
)

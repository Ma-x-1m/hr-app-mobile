package com.example.hr_app.domain.models

data class Message(
    val id: String,
    val conversationId: String,
    val senderId: String,
    val content: String,
    val sentAt: String,
    val isRead: Boolean
)

package com.example.hr_app.data.mappers

import com.example.hr_app.data.dto.responses.ConversationResponse
import com.example.hr_app.data.dto.responses.MessageResponse
import com.example.hr_app.domain.models.Conversation
import com.example.hr_app.domain.models.Message

fun ConversationResponse.toDomain(): Conversation = Conversation(
    id = id,
    applicationId = applicationId,
    createdAt = createdAt
)

fun MessageResponse.toDomain(): Message = Message(
    id = id,
    conversationId = conversationId,
    senderId = senderId,
    content = content,
    sentAt = sentAt,
    isRead = isRead
)

package com.example.hr_app.domain.repositories

import com.example.hr_app.domain.models.Conversation
import com.example.hr_app.domain.models.Message

interface ChatRepository {
    suspend fun getConversations(): Result<List<Conversation>>
    suspend fun getMessages(conversationId: String): Result<List<Message>>
    suspend fun sendMessage(conversationId: String, content: String): Result<Message>
}

package com.example.hr_app.data.repositories

import android.util.Log
import com.example.hr_app.data.api.ChatApi
import com.example.hr_app.data.dto.requests.SendMessageRequest
import com.example.hr_app.data.mappers.toDomain
import com.example.hr_app.domain.models.Conversation
import com.example.hr_app.domain.models.Message
import com.example.hr_app.domain.repositories.ChatRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ChatRepositoryImpl @Inject constructor(
    private val chatApi: ChatApi
) : ChatRepository {

    override suspend fun getConversations(): Result<List<Conversation>> = runCatching {
        try {
            chatApi.getConversations().map { it.toDomain() }
        } catch (e: Exception) {
            Log.e(TAG, "getConversations failed", e)
            throw e
        }
    }

    override suspend fun getMessages(conversationId: String): Result<List<Message>> = runCatching {
        try {
            chatApi.getMessages(conversationId).map { it.toDomain() }
        } catch (e: Exception) {
            Log.e(TAG, "getMessages($conversationId) failed", e)
            throw e
        }
    }

    override suspend fun sendMessage(
        conversationId: String,
        content: String
    ): Result<Message> = runCatching {
        try {
            chatApi.sendMessage(
                conversationId,
                SendMessageRequest(content = content)
            ).toDomain()
        } catch (e: Exception) {
            Log.e(TAG, "sendMessage($conversationId) failed", e)
            throw e
        }
    }

    private companion object {
        const val TAG = "ChatRepositoryImpl"
    }
}

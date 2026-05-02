package com.example.hr_app.data.api

import com.example.hr_app.data.dto.requests.SendMessageRequest
import com.example.hr_app.data.dto.responses.ConversationResponse
import com.example.hr_app.data.dto.responses.MessageResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ChatApi {
    @GET("conversations")
    suspend fun getConversations(): List<ConversationResponse>

    @GET("conversations/{id}/messages")
    suspend fun getMessages(@Path("id") conversationId: String): List<MessageResponse>

    @POST("conversations/{id}/messages")
    suspend fun sendMessage(
        @Path("id") conversationId: String,
        @Body request: SendMessageRequest
    ): MessageResponse
}

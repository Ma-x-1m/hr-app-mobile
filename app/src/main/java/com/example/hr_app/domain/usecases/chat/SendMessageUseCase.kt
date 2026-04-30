package com.example.hr_app.domain.usecases.chat

import com.example.hr_app.domain.models.Message
import com.example.hr_app.domain.repositories.ChatRepository

class SendMessageUseCase(private val chatRepository: ChatRepository) {
    suspend operator fun invoke(conversationId: String, content: String): Result<Message> {
        return chatRepository.sendMessage(conversationId, content)
    }
}

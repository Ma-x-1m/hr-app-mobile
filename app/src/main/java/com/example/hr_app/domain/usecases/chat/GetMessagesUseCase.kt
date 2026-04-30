package com.example.hr_app.domain.usecases.chat

import com.example.hr_app.domain.models.Message
import com.example.hr_app.domain.repositories.ChatRepository

class GetMessagesUseCase(private val chatRepository: ChatRepository) {
    suspend operator fun invoke(conversationId: String): Result<List<Message>> {
        return chatRepository.getMessages(conversationId)
    }
}

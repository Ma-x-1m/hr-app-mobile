package com.example.hr_app.domain.usecases.chat

import com.example.hr_app.domain.models.Message
import com.example.hr_app.domain.repositories.ChatRepository
import javax.inject.Inject

class GetMessagesUseCase @Inject constructor(
    private val chatRepository: ChatRepository
) {
    suspend operator fun invoke(conversationId: String): Result<List<Message>> {
        return chatRepository.getMessages(conversationId)
    }
}

package com.example.hr_app.domain.usecases.chat

import com.example.hr_app.domain.models.Conversation
import com.example.hr_app.domain.repositories.ChatRepository
import javax.inject.Inject

class GetConversationsUseCase @Inject constructor(
    private val chatRepository: ChatRepository
) {
    suspend operator fun invoke(): Result<List<Conversation>> {
        return chatRepository.getConversations()
    }
}

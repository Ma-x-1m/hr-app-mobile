package com.example.hr_app.presentation.screens.chat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hr_app.domain.models.Message
import com.example.hr_app.domain.usecases.auth.GetCurrentUserUseCase
import com.example.hr_app.domain.usecases.chat.GetMessagesUseCase
import com.example.hr_app.domain.usecases.chat.SendMessageUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ChatUiState(
    val isLoading: Boolean = false,
    val messages: List<Message> = emptyList(),
    val currentUserId: String? = null,
    val isSending: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val getMessagesUseCase: GetMessagesUseCase,
    private val sendMessageUseCase: SendMessageUseCase,
    private val getCurrentUserUseCase: GetCurrentUserUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(ChatUiState())
    val uiState: StateFlow<ChatUiState> = _uiState.asStateFlow()

    private var pollingJob: Job? = null

    fun loadMessages(conversationId: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)

            if (_uiState.value.currentUserId == null) {
                getCurrentUserUseCase().fold(
                    onSuccess = { user ->
                        _uiState.value = _uiState.value.copy(currentUserId = user.id)
                    },
                    onFailure = { throwable ->
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            error = throwable.message
                        )
                        return@launch
                    }
                )
            }

            getMessagesUseCase(conversationId).fold(
                onSuccess = { messages ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        messages = messages.toNewestFirst()
                    )
                },
                onFailure = { throwable ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = throwable.message
                    )
                }
            )
        }
    }

    fun sendMessage(conversationId: String, content: String) {
        val userId = _uiState.value.currentUserId ?: return
        val trimmed = content.trim()
        if (trimmed.isBlank()) return

        val tempId = "optimistic-${System.currentTimeMillis()}"
        val optimisticMessage = Message(
            id = tempId,
            conversationId = conversationId,
            senderId = userId,
            content = trimmed,
            sentAt = "",
            isRead = false
        )

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isSending = true,
                error = null,
                messages = listOf(optimisticMessage) + _uiState.value.messages
            )

            sendMessageUseCase(conversationId, trimmed).fold(
                onSuccess = { message ->
                    _uiState.value = _uiState.value.copy(
                        isSending = false,
                        messages = listOf(message) +
                            _uiState.value.messages.filter { it.id != tempId }
                    )
                },
                onFailure = { throwable ->
                    _uiState.value = _uiState.value.copy(
                        isSending = false,
                        error = throwable.message,
                        messages = _uiState.value.messages.filter { it.id != tempId }
                    )
                }
            )
        }
    }

    fun startPolling(conversationId: String) {
        pollingJob?.cancel()
        pollingJob = viewModelScope.launch {
            while (isActive) {
                delay(5_000)
                getMessagesUseCase(conversationId).fold(
                    onSuccess = { messages ->
                        _uiState.value = _uiState.value.copy(
                            messages = messages.toNewestFirst()
                        )
                    },
                    onFailure = { /* keep existing messages on poll failure */ }
                )
            }
        }
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }

    private fun List<Message>.toNewestFirst(): List<Message> {
        return sortedByDescending { it.sentAt }
    }
}

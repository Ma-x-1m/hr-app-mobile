package com.example.hr_app.presentation.screens.chat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hr_app.domain.models.Conversation
import com.example.hr_app.domain.models.UserRole
import com.example.hr_app.domain.usecases.auth.GetCurrentUserUseCase
import com.example.hr_app.domain.usecases.chat.GetConversationsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ConversationsUiState(
    val isLoading: Boolean = false,
    val conversations: List<Conversation> = emptyList(),
    val userRole: UserRole? = null,
    val error: String? = null
)

@HiltViewModel
class ConversationsViewModel @Inject constructor(
    private val getConversationsUseCase: GetConversationsUseCase,
    private val getCurrentUserUseCase: GetCurrentUserUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(ConversationsUiState())
    val uiState: StateFlow<ConversationsUiState> = _uiState.asStateFlow()

    init {
        loadConversations()
    }

    fun loadConversations() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)

            getCurrentUserUseCase().fold(
                onSuccess = { user ->
                    _uiState.value = _uiState.value.copy(userRole = user.role)
                },
                onFailure = { /* bottom bar falls back to applicant */ }
            )

            getConversationsUseCase().fold(
                onSuccess = { conversations ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        conversations = conversations
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

    fun refresh() {
        loadConversations()
    }
}

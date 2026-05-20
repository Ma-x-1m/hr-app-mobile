package com.example.hr_app.presentation.screens.applications

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hr_app.domain.models.Conversation
import com.example.hr_app.domain.models.JobApplication
import com.example.hr_app.domain.usecases.applications.GetMyApplicationsUseCase
import com.example.hr_app.domain.usecases.chat.GetConversationsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class MyApplicationsUiState(
    val isLoading: Boolean = false,
    val applications: List<JobApplication> = emptyList(),
    val error: String? = null
)

@HiltViewModel
class MyApplicationsViewModel @Inject constructor(
    private val getMyApplicationsUseCase: GetMyApplicationsUseCase,
    private val getConversationsUseCase: GetConversationsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(MyApplicationsUiState())
    val uiState: StateFlow<MyApplicationsUiState> = _uiState.asStateFlow()

    private var conversations: List<Conversation> = emptyList()

    init {
        loadApplications()
    }

    fun loadApplications() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)

            getConversationsUseCase().fold(
                onSuccess = { conversations = it },
                onFailure = { conversations = emptyList() }
            )

            getMyApplicationsUseCase().fold(
                onSuccess = { applications ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        applications = applications
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
        loadApplications()
    }

    fun getConversationId(applicationId: String): String? {
        return conversations.firstOrNull { it.applicationId == applicationId }?.id
    }
}

package com.example.hr_app.presentation.screens.applications

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hr_app.domain.models.Conversation
import com.example.hr_app.domain.models.JobApplication
import com.example.hr_app.domain.usecases.applications.GetMyApplicationsUseCase
import com.example.hr_app.domain.usecases.chat.GetConversationsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ApplicationsUiState(
    val isLoading: Boolean = false,
    val applications: List<JobApplication> = emptyList(),
    val conversations: List<Conversation> = emptyList(),
    val error: String? = null
)

@HiltViewModel
class ApplicationsViewModel @Inject constructor(
    private val getMyApplicationsUseCase: GetMyApplicationsUseCase,
    private val getConversationsUseCase: GetConversationsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(ApplicationsUiState())
    val uiState: StateFlow<ApplicationsUiState> = _uiState.asStateFlow()

    init {
        loadData()
    }

    fun loadData() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)

            val applicationsDeferred = async { getMyApplicationsUseCase() }
            val conversationsDeferred = async { getConversationsUseCase() }

            val applicationsResult = applicationsDeferred.await()
            val conversationsResult = conversationsDeferred.await()

            val conversations = conversationsResult.getOrElse { emptyList() }

            applicationsResult.fold(
                onSuccess = { applications ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        applications = applications,
                        conversations = conversations
                    )
                },
                onFailure = { throwable ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        applications = emptyList(),
                        conversations = conversations,
                        error = throwable.message
                    )
                }
            )
        }
    }

    fun getConversationId(applicationId: String): String? =
        uiState.value.conversations.find { it.applicationId == applicationId }?.id

    fun refresh() {
        loadData()
    }
}

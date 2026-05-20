package com.example.hr_app.presentation.screens.vacancies

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hr_app.domain.models.ApplicationStatus
import com.example.hr_app.domain.models.JobApplication
import com.example.hr_app.domain.usecases.applications.GetApplicationsByVacancyUseCase
import com.example.hr_app.domain.usecases.applications.UpdateApplicationStatusUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ApplicationsUiState(
    val isLoading: Boolean = false,
    val applications: List<JobApplication> = emptyList(),
    val error: String? = null,
    val statusUpdateSuccess: Boolean = false
)

@HiltViewModel
class VacancyApplicationsViewModel @Inject constructor(
    private val getApplicationsByVacancyUseCase: GetApplicationsByVacancyUseCase,
    private val updateApplicationStatusUseCase: UpdateApplicationStatusUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(ApplicationsUiState())
    val uiState: StateFlow<ApplicationsUiState> = _uiState.asStateFlow()

    fun loadApplications(vacancyId: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            getApplicationsByVacancyUseCase(vacancyId).fold(
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

    fun updateStatus(applicationId: String, status: ApplicationStatus, vacancyId: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isLoading = true,
                error = null,
                statusUpdateSuccess = false
            )
            updateApplicationStatusUseCase(applicationId, status).fold(
                onSuccess = {
                    loadApplications(vacancyId)
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        statusUpdateSuccess = true
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

    fun clearStatusUpdateSuccess() {
        _uiState.value = _uiState.value.copy(statusUpdateSuccess = false)
    }
}

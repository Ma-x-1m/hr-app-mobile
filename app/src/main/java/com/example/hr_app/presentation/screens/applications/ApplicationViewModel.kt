package com.example.hr_app.presentation.screens.applications

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hr_app.domain.models.Resume
import com.example.hr_app.domain.usecases.applications.ApplyToVacancyUseCase
import com.example.hr_app.domain.usecases.resumes.GetMyResumesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import retrofit2.HttpException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ApplicationUiState(
    val isApplying: Boolean = false,
    val resumes: List<Resume> = emptyList(),
    val resumesLoading: Boolean = false,
    val resumesError: String? = null,
    val applyError: String? = null,
    val applySuccess: Boolean = false
)

@HiltViewModel
class ApplicationViewModel @Inject constructor(
    private val applyToVacancyUseCase: ApplyToVacancyUseCase,
    private val getMyResumesUseCase: GetMyResumesUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(ApplicationUiState())
    val uiState: StateFlow<ApplicationUiState> = _uiState.asStateFlow()

    fun loadResumes() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                resumesLoading = true,
                resumesError = null
            )
            getMyResumesUseCase().fold(
                onSuccess = { resumes ->
                    _uiState.value = _uiState.value.copy(
                        resumesLoading = false,
                        resumes = resumes
                    )
                },
                onFailure = { throwable ->
                    _uiState.value = _uiState.value.copy(
                        resumesLoading = false,
                        resumesError = throwable.message
                    )
                }
            )
        }
    }

    fun apply(resumeId: String, vacancyId: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isApplying = true,
                applyError = null,
                applySuccess = false
            )
            applyToVacancyUseCase(resumeId, vacancyId).fold(
                onSuccess = {
                    _uiState.value = _uiState.value.copy(
                        isApplying = false,
                        applySuccess = true
                    )
                },
                onFailure = { throwable ->
                    val message = when {
                        throwable is HttpException && throwable.code() == 409 ->
                            "Вы уже откликались на эту вакансию"
                        else -> throwable.message ?: "Не удалось отправить отклик"
                    }
                    _uiState.value = _uiState.value.copy(
                        isApplying = false,
                        applyError = message
                    )
                }
            )
        }
    }

    fun clearApplySuccess() {
        _uiState.value = _uiState.value.copy(applySuccess = false)
    }

    fun clearApplyError() {
        _uiState.value = _uiState.value.copy(applyError = null)
    }
}

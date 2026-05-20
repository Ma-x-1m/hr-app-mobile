package com.example.hr_app.presentation.screens.resumes

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hr_app.domain.models.Resume
import com.example.hr_app.domain.models.ResumeStatus
import com.example.hr_app.domain.usecases.resumes.DeleteResumeUseCase
import com.example.hr_app.domain.usecases.resumes.GetMyResumesUseCase
import com.example.hr_app.domain.usecases.resumes.UpdateResumeUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ResumesUiState(
    val isLoading: Boolean = false,
    val resumes: List<Resume> = emptyList(),
    val error: String? = null
)

@HiltViewModel
class ResumesViewModel @Inject constructor(
    private val getMyResumesUseCase: GetMyResumesUseCase,
    private val deleteResumeUseCase: DeleteResumeUseCase,
    private val updateResumeUseCase: UpdateResumeUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(ResumesUiState())
    val uiState: StateFlow<ResumesUiState> = _uiState.asStateFlow()

    init {
        loadResumes()
    }

    fun loadResumes() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            getMyResumesUseCase().fold(
                onSuccess = { resumes ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        resumes = resumes
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

    fun deleteResume(id: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            deleteResumeUseCase(id).fold(
                onSuccess = { loadResumes() },
                onFailure = { throwable ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = throwable.message
                    )
                }
            )
        }
    }

    fun toggleVisibility(resume: Resume) {
        val newStatus = if (resume.status == ResumeStatus.ACTIVE) {
            ResumeStatus.HIDDEN
        } else {
            ResumeStatus.ACTIVE
        }
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            updateResumeUseCase(
                id = resume.id,
                title = resume.title,
                skills = resume.skills,
                experience = resume.experience,
                desiredSalary = resume.desiredSalary,
                status = newStatus
            ).fold(
                onSuccess = { loadResumes() },
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
        loadResumes()
    }
}

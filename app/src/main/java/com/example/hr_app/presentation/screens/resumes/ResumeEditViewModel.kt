package com.example.hr_app.presentation.screens.resumes

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hr_app.domain.models.Resume
import com.example.hr_app.domain.models.ResumeStatus
import com.example.hr_app.domain.usecases.resumes.CreateResumeUseCase
import com.example.hr_app.domain.usecases.resumes.GetResumeByIdUseCase
import com.example.hr_app.domain.usecases.resumes.UpdateResumeUseCase
import com.example.hr_app.domain.usecases.resumes.UploadResumeFileUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

data class ResumeEditUiState(
    val isLoading: Boolean = false,
    val isUploadingFile: Boolean = false,
    val isSaved: Boolean = false,
    val resume: Resume? = null,
    val selectedFileName: String? = null,
    val selectedFileUri: Uri? = null,
    val error: String? = null
)

@HiltViewModel
class ResumeEditViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val getResumeByIdUseCase: GetResumeByIdUseCase,
    private val createResumeUseCase: CreateResumeUseCase,
    private val updateResumeUseCase: UpdateResumeUseCase,
    private val uploadResumeFileUseCase: UploadResumeFileUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(ResumeEditUiState())
    val uiState: StateFlow<ResumeEditUiState> = _uiState.asStateFlow()

    fun loadResume(id: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            getResumeByIdUseCase(id).fold(
                onSuccess = { resume ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        resume = resume
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

    fun onFileSelected(uri: Uri, fileName: String) {
        _uiState.value = _uiState.value.copy(
            selectedFileUri = uri,
            selectedFileName = fileName
        )
    }

    fun saveResume(
        title: String,
        skills: String?,
        experience: String?,
        desiredSalary: Int?,
        status: ResumeStatus
    ) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isLoading = true,
                error = null,
                isSaved = false
            )
            val existing = _uiState.value.resume
            val saveResult = if (existing != null) {
                updateResumeUseCase(
                    id = existing.id,
                    title = title,
                    skills = skills,
                    experience = experience,
                    desiredSalary = desiredSalary,
                    status = status
                )
            } else {
                createResumeUseCase(
                    title = title,
                    skills = skills,
                    experience = experience,
                    desiredSalary = desiredSalary
                ).fold(
                    onSuccess = { created ->
                        if (created.status == status) {
                            Result.success(created)
                        } else {
                            updateResumeUseCase(
                                id = created.id,
                                title = title,
                                skills = skills,
                                experience = experience,
                                desiredSalary = desiredSalary,
                                status = status
                            )
                        }
                    },
                    onFailure = { Result.failure(it) }
                )
            }

            saveResult.fold(
                onSuccess = { savedResume ->
                    _uiState.value = _uiState.value.copy(resume = savedResume)
                    uploadFileIfNeeded(savedResume.id)
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

    private suspend fun uploadFileIfNeeded(resumeId: String) {
        val uri = _uiState.value.selectedFileUri
        if (uri == null) {
            _uiState.value = _uiState.value.copy(isLoading = false, isSaved = true)
            return
        }

        _uiState.value = _uiState.value.copy(isUploadingFile = true)
        try {
            val file = uriToFile(context, uri)
            uploadResumeFileUseCase(resumeId, file).fold(
                onSuccess = { resume ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        isUploadingFile = false,
                        isSaved = true,
                        resume = resume,
                        selectedFileUri = null,
                        selectedFileName = null
                    )
                },
                onFailure = { throwable ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        isUploadingFile = false,
                        error = throwable.message
                    )
                }
            )
        } catch (e: Exception) {
            _uiState.value = _uiState.value.copy(
                isLoading = false,
                isUploadingFile = false,
                error = e.message ?: "Не удалось прочитать файл"
            )
        }
    }

    fun clearSaved() {
        _uiState.value = _uiState.value.copy(isSaved = false)
    }

    private fun uriToFile(context: Context, uri: Uri): File {
        val inputStream = context.contentResolver.openInputStream(uri)
            ?: throw IllegalArgumentException("Не удалось открыть файл")
        val tempFile = File.createTempFile("resume_", ".pdf", context.cacheDir)
        tempFile.outputStream().use { output -> inputStream.use { it.copyTo(output) } }
        return tempFile
    }
}

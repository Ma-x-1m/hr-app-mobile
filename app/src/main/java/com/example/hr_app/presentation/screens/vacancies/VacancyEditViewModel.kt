package com.example.hr_app.presentation.screens.vacancies

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hr_app.domain.models.Vacancy
import com.example.hr_app.domain.models.VacancyStatus
import com.example.hr_app.domain.usecases.vacancies.CreateVacancyUseCase
import com.example.hr_app.domain.usecases.vacancies.GetVacancyByIdUseCase
import com.example.hr_app.domain.usecases.vacancies.UpdateVacancyUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class VacancyEditUiState(
    val isLoading: Boolean = false,
    val isSaved: Boolean = false,
    val vacancy: Vacancy? = null,
    val error: String? = null
)

@HiltViewModel
class VacancyEditViewModel @Inject constructor(
    private val getVacancyByIdUseCase: GetVacancyByIdUseCase,
    private val createVacancyUseCase: CreateVacancyUseCase,
    private val updateVacancyUseCase: UpdateVacancyUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(VacancyEditUiState())
    val uiState: StateFlow<VacancyEditUiState> = _uiState.asStateFlow()

    fun loadVacancy(id: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            getVacancyByIdUseCase(id).fold(
                onSuccess = { vacancy ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        vacancy = vacancy
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

    fun saveVacancy(
        title: String,
        description: String?,
        salaryFrom: Int?,
        salaryTo: Int?,
        requirements: String?,
        city: String?,
        experience: String?,
        status: VacancyStatus
    ) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null, isSaved = false)
            val existing = _uiState.value.vacancy
            if (existing != null) {
                updateVacancyUseCase(
                    id = existing.id,
                    title = title,
                    description = description,
                    salaryFrom = salaryFrom,
                    salaryTo = salaryTo,
                    requirements = requirements,
                    city = city,
                    experience = experience,
                    status = status
                ).fold(
                    onSuccess = { vacancy ->
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            isSaved = true,
                            vacancy = vacancy
                        )
                    },
                    onFailure = { throwable ->
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            error = throwable.message
                        )
                    }
                )
            } else {
                createVacancyUseCase(
                    title = title,
                    description = description,
                    salaryFrom = salaryFrom,
                    salaryTo = salaryTo,
                    requirements = requirements,
                    city = city,
                    experience = experience
                ).fold(
                    onSuccess = { created ->
                        if (created.status == status) {
                            _uiState.value = _uiState.value.copy(
                                isLoading = false,
                                isSaved = true,
                                vacancy = created
                            )
                        } else {
                            updateVacancyUseCase(
                                id = created.id,
                                title = title,
                                description = description,
                                salaryFrom = salaryFrom,
                                salaryTo = salaryTo,
                                requirements = requirements,
                                city = city,
                                experience = experience,
                                status = status
                            ).fold(
                                onSuccess = { vacancy ->
                                    _uiState.value = _uiState.value.copy(
                                        isLoading = false,
                                        isSaved = true,
                                        vacancy = vacancy
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
    }

    fun clearSaved() {
        _uiState.value = _uiState.value.copy(isSaved = false)
    }
}

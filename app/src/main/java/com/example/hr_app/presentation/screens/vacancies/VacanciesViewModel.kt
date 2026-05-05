package com.example.hr_app.presentation.screens.vacancies

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hr_app.domain.models.Vacancy
import com.example.hr_app.domain.usecases.vacancies.GetAllVacanciesUseCase
import com.example.hr_app.domain.usecases.vacancies.GetVacancyByIdUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class VacanciesUiState(
    val isLoading: Boolean = false,
    val vacancies: List<Vacancy> = emptyList(),
    val selectedVacancy: Vacancy? = null,
    val error: String? = null
)

@HiltViewModel
class VacanciesViewModel @Inject constructor(
    private val getAllVacanciesUseCase: GetAllVacanciesUseCase,
    private val getVacancyByIdUseCase: GetVacancyByIdUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(VacanciesUiState())
    val uiState: StateFlow<VacanciesUiState> = _uiState.asStateFlow()

    init {
        loadVacancies()
    }

    fun loadVacancies() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            getAllVacanciesUseCase().fold(
                onSuccess = { vacancies ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        vacancies = vacancies
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

    fun loadVacancyById(id: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            getVacancyByIdUseCase(id).fold(
                onSuccess = { vacancy ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        selectedVacancy = vacancy
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
        loadVacancies()
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }
}

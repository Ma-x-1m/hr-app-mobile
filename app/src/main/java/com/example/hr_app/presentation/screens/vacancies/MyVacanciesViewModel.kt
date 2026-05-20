package com.example.hr_app.presentation.screens.vacancies

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hr_app.domain.models.Vacancy
import com.example.hr_app.domain.usecases.vacancies.DeleteVacancyUseCase
import com.example.hr_app.domain.usecases.vacancies.GetMyVacanciesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class MyVacanciesUiState(
    val isLoading: Boolean = false,
    val vacancies: List<Vacancy> = emptyList(),
    val error: String? = null
)

@HiltViewModel
class MyVacanciesViewModel @Inject constructor(
    private val getMyVacanciesUseCase: GetMyVacanciesUseCase,
    private val deleteVacancyUseCase: DeleteVacancyUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(MyVacanciesUiState())
    val uiState: StateFlow<MyVacanciesUiState> = _uiState.asStateFlow()

    init {
        loadMyVacancies()
    }

    fun loadMyVacancies() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            getMyVacanciesUseCase().fold(
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

    fun deleteVacancy(id: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            deleteVacancyUseCase(id).fold(
                onSuccess = { loadMyVacancies() },
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
        loadMyVacancies()
    }
}

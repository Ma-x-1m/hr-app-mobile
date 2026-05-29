package com.example.hr_app.presentation.screens.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hr_app.domain.usecases.auth.DeleteAccountUseCase
import com.example.hr_app.domain.usecases.auth.LogoutUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class SettingsUiState(
    val isDeleted: Boolean = false,
    val isLoggedOut: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val deleteAccountUseCase: DeleteAccountUseCase,
    private val logoutUseCase: LogoutUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(SettingsUiState())
    val uiState: StateFlow<SettingsUiState> = _uiState.asStateFlow()

    fun deleteAccount() {
        viewModelScope.launch {
            _uiState.update { it.copy(error = null) }
            deleteAccountUseCase().fold(
                onSuccess = {
                    _uiState.update { it.copy(isDeleted = true) }
                },
                onFailure = { throwable ->
                    _uiState.update { it.copy(error = throwable.message) }
                }
            )
        }
    }

    fun logout() {
        viewModelScope.launch {
            _uiState.update { it.copy(error = null) }
            try {
                logoutUseCase()
                _uiState.update { it.copy(isLoggedOut = true) }
            } catch (e: Exception) {
                _uiState.update { it.copy(error = e.message) }
            }
        }
    }
}

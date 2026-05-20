package com.example.hr_app.presentation.screens.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hr_app.data.local.ThemeManager
import com.example.hr_app.data.local.ThemeMode
import com.example.hr_app.domain.usecases.auth.DeleteAccountUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class SettingsUiState(
    val currentTheme: ThemeMode = ThemeMode.SYSTEM,
    val isDeleted: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val deleteAccountUseCase: DeleteAccountUseCase,
    private val themeManager: ThemeManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(SettingsUiState())
    val uiState: StateFlow<SettingsUiState> = _uiState.asStateFlow()

    init {
        loadTheme()
    }

    private fun loadTheme() {
        viewModelScope.launch {
            themeManager.getThemeFlow().collect { mode ->
                _uiState.update { it.copy(currentTheme = mode) }
            }
        }
    }

    fun setTheme(mode: ThemeMode) {
        viewModelScope.launch {
            themeManager.setTheme(mode)
        }
    }

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
}

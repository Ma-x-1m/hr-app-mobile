package com.example.hr_app.presentation.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hr_app.domain.models.UserRole
import com.example.hr_app.domain.repositories.AuthRepository
import com.example.hr_app.presentation.navigation.Screen
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _startDestination = MutableStateFlow<String?>(null)
    val startDestination: StateFlow<String?> = _startDestination.asStateFlow()

    init {
        viewModelScope.launch {
            if (!authRepository.isLoggedIn()) {
                _startDestination.value = Screen.Login.route
                return@launch
            }
            authRepository.getCurrentUser().fold(
                onSuccess = { user ->
                    _startDestination.value = when (user.role) {
                        UserRole.APPLICANT -> Screen.VacanciesList.route
                        UserRole.EMPLOYER -> Screen.MyVacancies.route
                    }
                },
                onFailure = {
                    _startDestination.value = Screen.Login.route
                }
            )
        }
    }
}

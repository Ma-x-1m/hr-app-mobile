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

data class SplashState(
    val isLoading: Boolean = true,
    val startDestination: String? = null
)

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _state = MutableStateFlow(SplashState())
    val state: StateFlow<SplashState> = _state.asStateFlow()

    init {
        resolveStartDestination()
    }

    private fun resolveStartDestination() {
        viewModelScope.launch {
            val destination = if (authRepository.isLoggedIn()) {
                authRepository.getCurrentUser().fold(
                    onSuccess = { user ->
                        when (user.role) {
                            UserRole.APPLICANT -> Screen.VacanciesList.route
                            UserRole.EMPLOYER -> Screen.MyVacancies.route
                        }
                    },
                    onFailure = { Screen.Login.route }
                )
            } else {
                Screen.Login.route
            }
            _state.value = SplashState(isLoading = false, startDestination = destination)
        }
    }
}

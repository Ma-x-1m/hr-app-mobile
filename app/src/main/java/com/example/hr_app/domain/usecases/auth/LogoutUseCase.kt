package com.example.hr_app.domain.usecases.auth

import com.example.hr_app.domain.repositories.AuthRepository

class LogoutUseCase(private val authRepository: AuthRepository) {
    suspend operator fun invoke() {
        authRepository.logout()
    }
}

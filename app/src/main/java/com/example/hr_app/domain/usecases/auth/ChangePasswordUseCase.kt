package com.example.hr_app.domain.usecases.auth

import com.example.hr_app.domain.repositories.AuthRepository

class ChangePasswordUseCase(private val authRepository: AuthRepository) {
    suspend operator fun invoke(oldPassword: String, newPassword: String): Result<Unit> {
        return authRepository.changePassword(oldPassword, newPassword)
    }
}

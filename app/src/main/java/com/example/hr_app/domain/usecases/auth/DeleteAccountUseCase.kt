package com.example.hr_app.domain.usecases.auth

import com.example.hr_app.domain.repositories.AuthRepository

class DeleteAccountUseCase(private val authRepository: AuthRepository) {
    suspend operator fun invoke(): Result<Unit> {
        return authRepository.deleteAccount()
    }
}

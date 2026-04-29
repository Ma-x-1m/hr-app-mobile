package com.example.hr_app.domain.usecases.auth

import com.example.hr_app.domain.models.User
import com.example.hr_app.domain.repositories.AuthRepository

class GetCurrentUserUseCase(private val authRepository: AuthRepository) {
    suspend operator fun invoke(): Result<User> {
        return authRepository.getCurrentUser()
    }
}

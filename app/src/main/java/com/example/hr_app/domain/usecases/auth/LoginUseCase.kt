package com.example.hr_app.domain.usecases.auth

import com.example.hr_app.domain.models.User
import com.example.hr_app.domain.repositories.AuthRepository

class LoginUseCase(private val authRepository: AuthRepository) {
    suspend operator fun invoke(email: String, password: String): Result<User> {
        return authRepository.login(email, password)
    }
}

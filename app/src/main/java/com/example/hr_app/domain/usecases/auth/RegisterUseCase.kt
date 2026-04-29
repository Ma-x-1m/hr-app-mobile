package com.example.hr_app.domain.usecases.auth

import com.example.hr_app.domain.models.User
import com.example.hr_app.domain.models.UserRole
import com.example.hr_app.domain.repositories.AuthRepository

class RegisterUseCase(private val authRepository: AuthRepository) {
    suspend operator fun invoke(
        email: String,
        password: String,
        name: String,
        role: UserRole
    ): Result<User> {
        return authRepository.register(email, password, name, role)
    }
}

package com.example.hr_app.domain.usecases.auth

import com.example.hr_app.domain.models.User
import com.example.hr_app.domain.repositories.AuthRepository
import javax.inject.Inject

class LoginUseCase @Inject constructor(private val authRepository: AuthRepository) {
    suspend operator fun invoke(email: String, password: String): Result<User> {
        return authRepository.login(email, password)
    }
}

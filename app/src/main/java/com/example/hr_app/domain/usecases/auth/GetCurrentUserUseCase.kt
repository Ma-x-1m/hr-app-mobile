package com.example.hr_app.domain.usecases.auth

import com.example.hr_app.domain.models.User
import com.example.hr_app.domain.repositories.AuthRepository
import javax.inject.Inject

class GetCurrentUserUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(): Result<User> {
        return authRepository.getCurrentUser()
    }
}

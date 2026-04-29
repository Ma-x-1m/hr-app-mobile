package com.example.hr_app.domain.repositories

import com.example.hr_app.domain.models.User
import com.example.hr_app.domain.models.UserRole

interface AuthRepository {
    suspend fun login(email: String, password: String): Result<User>
    suspend fun register(email: String, password: String, name: String, role: UserRole): Result<User>
    suspend fun logout()
    suspend fun getCurrentUser(): Result<User>
    suspend fun changePassword(oldPassword: String, newPassword: String): Result<Unit>
    suspend fun deleteAccount(): Result<Unit>
    suspend fun isLoggedIn(): Boolean
}

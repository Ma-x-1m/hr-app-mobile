package com.example.hr_app.data.repositories

import android.util.Log
import com.example.hr_app.data.api.AuthApi
import com.example.hr_app.data.dto.requests.ChangePasswordRequest
import com.example.hr_app.data.dto.requests.LoginRequest
import com.example.hr_app.data.dto.requests.RegisterRequest
import com.example.hr_app.data.local.TokenStorage
import com.example.hr_app.data.mappers.toDomain
import com.example.hr_app.domain.models.User
import com.example.hr_app.domain.models.UserRole
import com.example.hr_app.domain.repositories.AuthRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepositoryImpl @Inject constructor(
    private val authApi: AuthApi,
    private val tokenStorage: TokenStorage
) : AuthRepository {

    override suspend fun login(email: String, password: String): Result<User> = runCatching {
        try {
            val response = authApi.login(LoginRequest(email = email, password = password))
            tokenStorage.saveToken(response.token)
            response.user.toDomain()
        } catch (e: Exception) {
            Log.e(TAG, "login failed", e)
            throw e
        }
    }

    override suspend fun register(
        email: String,
        password: String,
        name: String,
        role: UserRole
    ): Result<User> = runCatching {
        try {
            val response = authApi.register(
                RegisterRequest(
                    email = email,
                    password = password,
                    name = name,
                    role = role.value
                )
            )
            tokenStorage.saveToken(response.token)
            response.user.toDomain()
        } catch (e: Exception) {
            Log.e(TAG, "register failed", e)
            throw e
        }
    }

    override suspend fun logout() {
        tokenStorage.clearToken()
    }

    override suspend fun getCurrentUser(): Result<User> = runCatching {
        try {
            authApi.getCurrentUser().toDomain()
        } catch (e: Exception) {
            Log.e(TAG, "getCurrentUser failed", e)
            throw e
        }
    }

    override suspend fun changePassword(
        oldPassword: String,
        newPassword: String
    ): Result<Unit> = runCatching {
        try {
            authApi.changePassword(
                ChangePasswordRequest(oldPassword = oldPassword, newPassword = newPassword)
            )
        } catch (e: Exception) {
            Log.e(TAG, "changePassword failed", e)
            throw e
        }
    }

    override suspend fun deleteAccount(): Result<Unit> = runCatching {
        try {
            authApi.deleteAccount()
            tokenStorage.clearToken()
        } catch (e: Exception) {
            Log.e(TAG, "deleteAccount failed", e)
            throw e
        }
    }

    override suspend fun isLoggedIn(): Boolean =
        !tokenStorage.getToken().isNullOrBlank()

    private companion object {
        const val TAG = "AuthRepositoryImpl"
    }
}

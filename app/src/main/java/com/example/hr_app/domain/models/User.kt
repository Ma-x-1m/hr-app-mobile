package com.example.hr_app.domain.models

data class User(
    val id: String,
    val email: String,
    val name: String,
    val role: UserRole,
    val createdAt: String
)

enum class UserRole(val value: String) {
    APPLICANT("applicant"),
    EMPLOYER("employer");

    companion object {
        fun fromString(value: String): UserRole {
            return entries.first { it.value.equals(value, ignoreCase = true) }
        }
    }
}

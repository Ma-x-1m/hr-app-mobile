package com.example.hr_app.domain.models

enum class UserRole(val value: String) {
    APPLICANT("applicant"),
    EMPLOYER("employer");

    companion object {
        fun fromString(value: String): UserRole {
            return entries.first { it.value.equals(value, ignoreCase = true) }
        }
    }
}

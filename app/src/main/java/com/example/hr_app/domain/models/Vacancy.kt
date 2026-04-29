package com.example.hr_app.domain.models

data class Vacancy(
    val id: String,
    val employerId: String,
    val title: String,
    val description: String?,
    val salaryFrom: Int?,
    val salaryTo: Int?,
    val requirements: String?,
    val city: String?,
    val experience: String?,
    val status: VacancyStatus,
    val createdAt: String
)

enum class VacancyStatus(val value: String) {
    OPEN("open"),
    CLOSED("closed"),
    DRAFT("draft");

    companion object {
        fun fromString(value: String): VacancyStatus {
            return entries.first { it.value.equals(value, ignoreCase = true) }
        }
    }
}

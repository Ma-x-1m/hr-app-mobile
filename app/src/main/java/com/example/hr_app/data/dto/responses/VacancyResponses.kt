package com.example.hr_app.data.dto.responses

import kotlinx.serialization.Serializable

@Serializable
data class VacancyResponse(
    val id: String,
    val employerId: String,
    val title: String,
    val description: String? = null,
    val salaryFrom: Int? = null,
    val salaryTo: Int? = null,
    val requirements: String? = null,
    val city: String? = null,
    val experience: String? = null,
    val status: String,
    val createdAt: String
)

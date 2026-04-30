package com.example.hr_app.data.dto.requests

import kotlinx.serialization.Serializable

@Serializable
data class CreateVacancyRequest(
    val title: String,
    val description: String? = null,
    val salaryFrom: Int? = null,
    val salaryTo: Int? = null,
    val requirements: String? = null,
    val city: String? = null,
    val experience: String? = null
)

@Serializable
data class UpdateVacancyRequest(
    val title: String? = null,
    val description: String? = null,
    val salaryFrom: Int? = null,
    val salaryTo: Int? = null,
    val requirements: String? = null,
    val city: String? = null,
    val experience: String? = null,
    val status: String? = null
)

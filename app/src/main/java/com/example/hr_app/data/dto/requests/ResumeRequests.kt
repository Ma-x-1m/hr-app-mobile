package com.example.hr_app.data.dto.requests

import kotlinx.serialization.Serializable

@Serializable
data class CreateResumeRequest(
    val title: String,
    val skills: String? = null,
    val experience: String? = null,
    val desiredSalary: Int? = null
)

@Serializable
data class UpdateResumeRequest(
    val title: String? = null,
    val skills: String? = null,
    val experience: String? = null,
    val desiredSalary: Int? = null,
    val status: String? = null
)

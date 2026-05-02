package com.example.hr_app.data.dto.responses

import kotlinx.serialization.Serializable

@Serializable
data class ResumeResponse(
    val id: String,
    val applicantId: String,
    val title: String,
    val skills: String? = null,
    val experience: String? = null,
    val desiredSalary: Int? = null,
    val resumeFileUrl: String? = null,
    val status: String,
    val createdAt: String
)

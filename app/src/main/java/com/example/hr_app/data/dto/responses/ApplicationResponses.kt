package com.example.hr_app.data.dto.responses

import kotlinx.serialization.Serializable

@Serializable
data class JobApplicationResponse(
    val id: String,
    val resumeId: String,
    val vacancyId: String,
    val status: String,
    val createdAt: String
)

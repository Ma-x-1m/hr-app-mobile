package com.example.hr_app.data.dto.requests

import kotlinx.serialization.Serializable

@Serializable
data class CreateJobApplicationRequest(
    val resumeId: String,
    val vacancyId: String
)

@Serializable
data class UpdateApplicationStatusRequest(
    val status: String
)

package com.example.hr_app.data.mappers

import com.example.hr_app.data.dto.responses.JobApplicationResponse
import com.example.hr_app.domain.models.ApplicationStatus
import com.example.hr_app.domain.models.JobApplication

fun JobApplicationResponse.toDomain(): JobApplication = JobApplication(
    id = id,
    resumeId = resumeId,
    vacancyId = vacancyId,
    status = ApplicationStatus.valueOf(status.uppercase()),
    createdAt = createdAt
)

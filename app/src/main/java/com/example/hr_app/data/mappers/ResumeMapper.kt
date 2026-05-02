package com.example.hr_app.data.mappers

import com.example.hr_app.data.dto.responses.ResumeResponse
import com.example.hr_app.domain.models.Resume
import com.example.hr_app.domain.models.ResumeStatus

fun ResumeResponse.toDomain(): Resume = Resume(
    id = id,
    applicantId = applicantId,
    title = title,
    skills = skills,
    experience = experience,
    desiredSalary = desiredSalary,
    resumeFileUrl = resumeFileUrl,
    status = ResumeStatus.valueOf(status.uppercase()),
    createdAt = createdAt
)

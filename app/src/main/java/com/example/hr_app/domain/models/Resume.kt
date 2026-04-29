package com.example.hr_app.domain.models

data class Resume(
    val id: String,
    val applicantId: String,
    val title: String,
    val skills: String?,
    val experience: String?,
    val desiredSalary: Int?,
    val resumeFileUrl: String?,
    val status: ResumeStatus,
    val createdAt: String
)

enum class ResumeStatus{
    ACTIVE,
    HIDDEN
}
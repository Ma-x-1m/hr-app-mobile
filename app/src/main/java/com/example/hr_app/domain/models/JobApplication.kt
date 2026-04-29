package com.example.hr_app.domain.models

data class JobApplication(
    val id: String,
    val resumeId: String,
    val vacancyId: String,
    val status: ApplicationStatus,
    val createdAt: String
)

enum class ApplicationStatus {
    PENDING,
    VIEWED,
    REJECTED,
    ACCEPTED
}

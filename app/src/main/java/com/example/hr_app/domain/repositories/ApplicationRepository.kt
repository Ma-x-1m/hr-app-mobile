package com.example.hr_app.domain.repositories

import com.example.hr_app.domain.models.ApplicationStatus
import com.example.hr_app.domain.models.JobApplication

interface ApplicationRepository {
    suspend fun applyToVacancy(resumeId: String, vacancyId: String): Result<JobApplication>
    suspend fun getMyApplications(): Result<List<JobApplication>>
    suspend fun getApplicationsByVacancy(vacancyId: String): Result<List<JobApplication>>
    suspend fun updateApplicationStatus(id: String, status: ApplicationStatus): Result<JobApplication>
}

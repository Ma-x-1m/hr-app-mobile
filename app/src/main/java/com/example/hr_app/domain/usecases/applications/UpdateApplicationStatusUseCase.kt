package com.example.hr_app.domain.usecases.applications

import com.example.hr_app.domain.models.ApplicationStatus
import com.example.hr_app.domain.models.JobApplication
import com.example.hr_app.domain.repositories.ApplicationRepository

class UpdateApplicationStatusUseCase(private val applicationRepository: ApplicationRepository) {
    suspend operator fun invoke(id: String, status: ApplicationStatus): Result<JobApplication> {
        return applicationRepository.updateApplicationStatus(id, status)
    }
}

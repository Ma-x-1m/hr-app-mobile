package com.example.hr_app.domain.usecases.applications

import com.example.hr_app.domain.models.JobApplication
import com.example.hr_app.domain.repositories.ApplicationRepository

class GetMyApplicationsUseCase(private val applicationRepository: ApplicationRepository) {
    suspend operator fun invoke(): Result<List<JobApplication>> {
        return applicationRepository.getMyApplications()
    }
}

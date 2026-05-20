package com.example.hr_app.domain.usecases.applications

import com.example.hr_app.domain.models.JobApplication
import com.example.hr_app.domain.repositories.ApplicationRepository
import javax.inject.Inject

class ApplyToVacancyUseCase @Inject constructor(
    private val applicationRepository: ApplicationRepository
) {
    suspend operator fun invoke(resumeId: String, vacancyId: String): Result<JobApplication> {
        return applicationRepository.applyToVacancy(resumeId, vacancyId)
    }
}

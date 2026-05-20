package com.example.hr_app.domain.usecases.applications

import com.example.hr_app.domain.models.JobApplication
import com.example.hr_app.domain.repositories.ApplicationRepository
import javax.inject.Inject

class GetApplicationsByVacancyUseCase @Inject constructor(
    private val applicationRepository: ApplicationRepository
) {
    suspend operator fun invoke(vacancyId: String): Result<List<JobApplication>> {
        return applicationRepository.getApplicationsByVacancy(vacancyId)
    }
}

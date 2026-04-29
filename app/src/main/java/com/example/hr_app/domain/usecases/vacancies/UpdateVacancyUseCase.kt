package com.example.hr_app.domain.usecases.vacancies

import com.example.hr_app.domain.models.Vacancy
import com.example.hr_app.domain.models.VacancyStatus
import com.example.hr_app.domain.repositories.VacancyRepository

class UpdateVacancyUseCase(private val vacancyRepository: VacancyRepository) {
    suspend operator fun invoke(
        id: String,
        title: String,
        description: String?,
        salaryFrom: Int?,
        salaryTo: Int?,
        requirements: String?,
        city: String?,
        experience: String?,
        status: VacancyStatus
    ): Result<Vacancy> {
        return vacancyRepository.updateVacancy(
            id = id,
            title = title,
            description = description,
            salaryFrom = salaryFrom,
            salaryTo = salaryTo,
            requirements = requirements,
            city = city,
            experience = experience,
            status = status
        )
    }
}

package com.example.hr_app.domain.usecases.vacancies

import com.example.hr_app.domain.models.Vacancy
import com.example.hr_app.domain.repositories.VacancyRepository

class CreateVacancyUseCase(private val vacancyRepository: VacancyRepository) {
    suspend operator fun invoke(
        title: String,
        description: String?,
        salaryFrom: Int?,
        salaryTo: Int?,
        requirements: String?,
        city: String?,
        experience: String?
    ): Result<Vacancy> {
        return vacancyRepository.createVacancy(
            title = title,
            description = description,
            salaryFrom = salaryFrom,
            salaryTo = salaryTo,
            requirements = requirements,
            city = city,
            experience = experience
        )
    }
}

package com.example.hr_app.domain.usecases.vacancies

import com.example.hr_app.domain.models.Vacancy
import com.example.hr_app.domain.repositories.VacancyRepository

class GetVacancyByIdUseCase(private val vacancyRepository: VacancyRepository) {
    suspend operator fun invoke(id: String): Result<Vacancy> {
        return vacancyRepository.getVacancyById(id)
    }
}

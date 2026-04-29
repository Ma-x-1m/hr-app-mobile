package com.example.hr_app.domain.usecases.vacancies

import com.example.hr_app.domain.models.Vacancy
import com.example.hr_app.domain.repositories.VacancyRepository

class GetAllVacanciesUseCase(private val vacancyRepository: VacancyRepository) {
    suspend operator fun invoke(): Result<List<Vacancy>> {
        return vacancyRepository.getAllVacancies()
    }
}

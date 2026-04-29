package com.example.hr_app.domain.usecases.vacancies

import com.example.hr_app.domain.repositories.VacancyRepository

class DeleteVacancyUseCase(private val vacancyRepository: VacancyRepository) {
    suspend operator fun invoke(id: String): Result<Unit> {
        return vacancyRepository.deleteVacancy(id)
    }
}

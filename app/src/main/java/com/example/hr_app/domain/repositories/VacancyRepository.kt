package com.example.hr_app.domain.repositories

import com.example.hr_app.domain.models.Vacancy
import com.example.hr_app.domain.models.VacancyStatus

interface VacancyRepository {
    suspend fun getAllVacancies(): Result<List<Vacancy>>
    suspend fun getVacancyById(id: String): Result<Vacancy>
    suspend fun getMyVacancies(): Result<List<Vacancy>>
    suspend fun createVacancy(
        title: String,
        description: String?,
        salaryFrom: Int?,
        salaryTo: Int?,
        requirements: String?,
        city: String?,
        experience: String?
    ): Result<Vacancy>
    suspend fun updateVacancy(
        id: String,
        title: String,
        description: String?,
        salaryFrom: Int?,
        salaryTo: Int?,
        requirements: String?,
        city: String?,
        experience: String?,
        status: VacancyStatus
    ): Result<Vacancy>
    suspend fun deleteVacancy(id: String): Result<Unit>
}

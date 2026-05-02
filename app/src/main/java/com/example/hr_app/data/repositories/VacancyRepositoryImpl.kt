package com.example.hr_app.data.repositories

import android.util.Log
import com.example.hr_app.data.api.VacancyApi
import com.example.hr_app.data.dto.requests.CreateVacancyRequest
import com.example.hr_app.data.dto.requests.UpdateVacancyRequest
import com.example.hr_app.data.mappers.toDomain
import com.example.hr_app.domain.models.Vacancy
import com.example.hr_app.domain.models.VacancyStatus
import com.example.hr_app.domain.repositories.VacancyRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class VacancyRepositoryImpl @Inject constructor(
    private val vacancyApi: VacancyApi
) : VacancyRepository {

    override suspend fun getAllVacancies(): Result<List<Vacancy>> = runCatching {
        try {
            vacancyApi.getAllVacancies().map { it.toDomain() }
        } catch (e: Exception) {
            Log.e(TAG, "getAllVacancies failed", e)
            throw e
        }
    }

    override suspend fun getVacancyById(id: String): Result<Vacancy> = runCatching {
        try {
            vacancyApi.getVacancyById(id).toDomain()
        } catch (e: Exception) {
            Log.e(TAG, "getVacancyById($id) failed", e)
            throw e
        }
    }

    override suspend fun getMyVacancies(): Result<List<Vacancy>> = runCatching {
        try {
            vacancyApi.getMyVacancies().map { it.toDomain() }
        } catch (e: Exception) {
            Log.e(TAG, "getMyVacancies failed", e)
            throw e
        }
    }

    override suspend fun createVacancy(
        title: String,
        description: String?,
        salaryFrom: Int?,
        salaryTo: Int?,
        requirements: String?,
        city: String?,
        experience: String?
    ): Result<Vacancy> = runCatching {
        try {
            vacancyApi.createVacancy(
                CreateVacancyRequest(
                    title = title,
                    description = description,
                    salaryFrom = salaryFrom,
                    salaryTo = salaryTo,
                    requirements = requirements,
                    city = city,
                    experience = experience
                )
            ).toDomain()
        } catch (e: Exception) {
            Log.e(TAG, "createVacancy failed", e)
            throw e
        }
    }

    override suspend fun updateVacancy(
        id: String,
        title: String,
        description: String?,
        salaryFrom: Int?,
        salaryTo: Int?,
        requirements: String?,
        city: String?,
        experience: String?,
        status: VacancyStatus
    ): Result<Vacancy> = runCatching {
        try {
            vacancyApi.updateVacancy(
                id,
                UpdateVacancyRequest(
                    title = title,
                    description = description,
                    salaryFrom = salaryFrom,
                    salaryTo = salaryTo,
                    requirements = requirements,
                    city = city,
                    experience = experience,
                    status = status.value
                )
            ).toDomain()
        } catch (e: Exception) {
            Log.e(TAG, "updateVacancy($id) failed", e)
            throw e
        }
    }

    override suspend fun deleteVacancy(id: String): Result<Unit> = runCatching {
        try {
            vacancyApi.deleteVacancy(id)
        } catch (e: Exception) {
            Log.e(TAG, "deleteVacancy($id) failed", e)
            throw e
        }
    }

    private companion object {
        const val TAG = "VacancyRepositoryImpl"
    }
}

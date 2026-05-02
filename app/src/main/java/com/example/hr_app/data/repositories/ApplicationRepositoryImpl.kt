package com.example.hr_app.data.repositories

import android.util.Log
import com.example.hr_app.data.api.ApplicationApi
import com.example.hr_app.data.dto.requests.CreateJobApplicationRequest
import com.example.hr_app.data.dto.requests.UpdateApplicationStatusRequest
import com.example.hr_app.data.mappers.toDomain
import com.example.hr_app.domain.models.ApplicationStatus
import com.example.hr_app.domain.models.JobApplication
import com.example.hr_app.domain.repositories.ApplicationRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ApplicationRepositoryImpl @Inject constructor(
    private val applicationApi: ApplicationApi
) : ApplicationRepository {

    override suspend fun applyToVacancy(
        resumeId: String,
        vacancyId: String
    ): Result<JobApplication> = runCatching {
        try {
            applicationApi.applyToVacancy(
                CreateJobApplicationRequest(resumeId = resumeId, vacancyId = vacancyId)
            ).toDomain()
        } catch (e: Exception) {
            Log.e(TAG, "applyToVacancy(resume=$resumeId, vacancy=$vacancyId) failed", e)
            throw e
        }
    }

    override suspend fun getMyApplications(): Result<List<JobApplication>> = runCatching {
        try {
            applicationApi.getMyApplications().map { it.toDomain() }
        } catch (e: Exception) {
            Log.e(TAG, "getMyApplications failed", e)
            throw e
        }
    }

    override suspend fun getApplicationsByVacancy(
        vacancyId: String
    ): Result<List<JobApplication>> = runCatching {
        try {
            applicationApi.getApplicationsByVacancy(vacancyId).map { it.toDomain() }
        } catch (e: Exception) {
            Log.e(TAG, "getApplicationsByVacancy($vacancyId) failed", e)
            throw e
        }
    }

    override suspend fun updateApplicationStatus(
        id: String,
        status: ApplicationStatus
    ): Result<JobApplication> = runCatching {
        try {
            applicationApi.updateApplicationStatus(
                id,
                UpdateApplicationStatusRequest(status = status.name.lowercase())
            ).toDomain()
        } catch (e: Exception) {
            Log.e(TAG, "updateApplicationStatus($id) failed", e)
            throw e
        }
    }

    private companion object {
        const val TAG = "ApplicationRepositoryImpl"
    }
}

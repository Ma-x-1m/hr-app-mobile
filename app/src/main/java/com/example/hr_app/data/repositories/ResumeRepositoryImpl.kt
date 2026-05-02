package com.example.hr_app.data.repositories

import android.util.Log
import com.example.hr_app.data.api.ResumeApi
import com.example.hr_app.data.dto.requests.CreateResumeRequest
import com.example.hr_app.data.dto.requests.UpdateResumeRequest
import com.example.hr_app.data.mappers.toDomain
import com.example.hr_app.domain.models.Resume
import com.example.hr_app.domain.models.ResumeStatus
import com.example.hr_app.domain.repositories.ResumeRepository
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ResumeRepositoryImpl @Inject constructor(
    private val resumeApi: ResumeApi
) : ResumeRepository {

    override suspend fun getMyResumes(): Result<List<Resume>> = runCatching {
        try {
            resumeApi.getMyResumes().map { it.toDomain() }
        } catch (e: Exception) {
            Log.e(TAG, "getMyResumes failed", e)
            throw e
        }
    }

    override suspend fun getResumeById(id: String): Result<Resume> = runCatching {
        try {
            resumeApi.getResumeById(id).toDomain()
        } catch (e: Exception) {
            Log.e(TAG, "getResumeById($id) failed", e)
            throw e
        }
    }

    override suspend fun createResume(
        title: String,
        skills: String?,
        experience: String?,
        desiredSalary: Int?
    ): Result<Resume> = runCatching {
        try {
            resumeApi.createResume(
                CreateResumeRequest(
                    title = title,
                    skills = skills,
                    experience = experience,
                    desiredSalary = desiredSalary
                )
            ).toDomain()
        } catch (e: Exception) {
            Log.e(TAG, "createResume failed", e)
            throw e
        }
    }

    override suspend fun updateResume(
        id: String,
        title: String,
        skills: String?,
        experience: String?,
        desiredSalary: Int?,
        status: ResumeStatus
    ): Result<Resume> = runCatching {
        try {
            resumeApi.updateResume(
                id,
                UpdateResumeRequest(
                    title = title,
                    skills = skills,
                    experience = experience,
                    desiredSalary = desiredSalary,
                    status = status.name.lowercase()
                )
            ).toDomain()
        } catch (e: Exception) {
            Log.e(TAG, "updateResume($id) failed", e)
            throw e
        }
    }

    override suspend fun deleteResume(id: String): Result<Unit> = runCatching {
        try {
            resumeApi.deleteResume(id)
        } catch (e: Exception) {
            Log.e(TAG, "deleteResume($id) failed", e)
            throw e
        }
    }

    override suspend fun uploadResumeFile(id: String, file: File): Result<Resume> = runCatching {
        try {
            val mediaType = "application/octet-stream".toMediaTypeOrNull()
            val requestBody = file.asRequestBody(mediaType)
            val part = MultipartBody.Part.createFormData("file", file.name, requestBody)
            resumeApi.uploadFile(id, part).toDomain()
        } catch (e: Exception) {
            Log.e(TAG, "uploadResumeFile($id) failed", e)
            throw e
        }
    }

    private companion object {
        const val TAG = "ResumeRepositoryImpl"
    }
}

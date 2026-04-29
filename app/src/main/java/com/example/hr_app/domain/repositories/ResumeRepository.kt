package com.example.hr_app.domain.repositories

import com.example.hr_app.domain.models.Resume
import com.example.hr_app.domain.models.ResumeStatus
import java.io.File

interface ResumeRepository {
    suspend fun getMyResumes(): Result<List<Resume>>
    suspend fun getResumeById(id: String): Result<Resume>
    suspend fun createResume(
        title: String,
        skills: String?,
        experience: String?,
        desiredSalary: Int?
    ): Result<Resume>
    suspend fun updateResume(
        id: String,
        title: String,
        skills: String?,
        experience: String?,
        desiredSalary: Int?,
        status: ResumeStatus
    ): Result<Resume>
    suspend fun deleteResume(id: String): Result<Unit>
    suspend fun uploadResumeFile(id: String, file: File): Result<Resume>
}

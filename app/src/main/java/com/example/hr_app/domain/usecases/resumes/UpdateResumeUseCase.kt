package com.example.hr_app.domain.usecases.resumes

import com.example.hr_app.domain.models.Resume
import com.example.hr_app.domain.models.ResumeStatus
import com.example.hr_app.domain.repositories.ResumeRepository

class UpdateResumeUseCase(private val resumeRepository: ResumeRepository) {
    suspend operator fun invoke(
        id: String,
        title: String,
        skills: String?,
        experience: String?,
        desiredSalary: Int?,
        status: ResumeStatus
    ): Result<Resume> {
        return resumeRepository.updateResume(
            id = id,
            title = title,
            skills = skills,
            experience = experience,
            desiredSalary = desiredSalary,
            status = status
        )
    }
}

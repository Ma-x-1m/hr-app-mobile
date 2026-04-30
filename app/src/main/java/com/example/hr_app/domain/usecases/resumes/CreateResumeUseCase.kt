package com.example.hr_app.domain.usecases.resumes

import com.example.hr_app.domain.models.Resume
import com.example.hr_app.domain.repositories.ResumeRepository

class CreateResumeUseCase(private val resumeRepository: ResumeRepository) {
    suspend operator fun invoke(
        title: String,
        skills: String?,
        experience: String?,
        desiredSalary: Int?
    ): Result<Resume> {
        return resumeRepository.createResume(
            title = title,
            skills = skills,
            experience = experience,
            desiredSalary = desiredSalary
        )
    }
}

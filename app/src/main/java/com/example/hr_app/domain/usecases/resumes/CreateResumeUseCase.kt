package com.example.hr_app.domain.usecases.resumes

import com.example.hr_app.domain.models.Resume
import com.example.hr_app.domain.repositories.ResumeRepository
import javax.inject.Inject

class CreateResumeUseCase @Inject constructor(
    private val resumeRepository: ResumeRepository
) {
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

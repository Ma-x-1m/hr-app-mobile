package com.example.hr_app.domain.usecases.resumes

import com.example.hr_app.domain.models.Resume
import com.example.hr_app.domain.repositories.ResumeRepository
import javax.inject.Inject

class GetResumeByIdUseCase @Inject constructor(
    private val resumeRepository: ResumeRepository
) {
    suspend operator fun invoke(id: String): Result<Resume> {
        return resumeRepository.getResumeById(id)
    }
}

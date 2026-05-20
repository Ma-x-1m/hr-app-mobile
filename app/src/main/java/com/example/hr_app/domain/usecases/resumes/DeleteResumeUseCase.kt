package com.example.hr_app.domain.usecases.resumes

import com.example.hr_app.domain.repositories.ResumeRepository
import javax.inject.Inject

class DeleteResumeUseCase @Inject constructor(
    private val resumeRepository: ResumeRepository
) {
    suspend operator fun invoke(id: String): Result<Unit> {
        return resumeRepository.deleteResume(id)
    }
}

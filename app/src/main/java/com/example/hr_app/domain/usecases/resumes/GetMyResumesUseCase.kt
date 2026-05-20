package com.example.hr_app.domain.usecases.resumes

import com.example.hr_app.domain.models.Resume
import com.example.hr_app.domain.repositories.ResumeRepository
import javax.inject.Inject

class GetMyResumesUseCase @Inject constructor(
    private val resumeRepository: ResumeRepository
) {
    suspend operator fun invoke(): Result<List<Resume>> {
        return resumeRepository.getMyResumes()
    }
}

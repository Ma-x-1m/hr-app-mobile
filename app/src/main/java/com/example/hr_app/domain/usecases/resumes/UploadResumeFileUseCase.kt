package com.example.hr_app.domain.usecases.resumes

import com.example.hr_app.domain.models.Resume
import com.example.hr_app.domain.repositories.ResumeRepository
import java.io.File

class UploadResumeFileUseCase(private val resumeRepository: ResumeRepository) {
    suspend operator fun invoke(id: String, file: File): Result<Resume> {
        return resumeRepository.uploadResumeFile(id, file)
    }
}

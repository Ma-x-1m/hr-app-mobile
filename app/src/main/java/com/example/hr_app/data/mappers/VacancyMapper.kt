package com.example.hr_app.data.mappers

import com.example.hr_app.data.dto.responses.VacancyResponse
import com.example.hr_app.domain.models.Vacancy
import com.example.hr_app.domain.models.VacancyStatus

fun VacancyResponse.toDomain(): Vacancy = Vacancy(
    id = id,
    employerId = employerId,
    title = title,
    description = description,
    salaryFrom = salaryFrom,
    salaryTo = salaryTo,
    requirements = requirements,
    city = city,
    experience = experience,
    status = VacancyStatus.fromString(status),
    createdAt = createdAt
)

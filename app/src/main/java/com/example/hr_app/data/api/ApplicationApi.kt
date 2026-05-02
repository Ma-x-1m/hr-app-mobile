package com.example.hr_app.data.api

import com.example.hr_app.data.dto.requests.CreateJobApplicationRequest
import com.example.hr_app.data.dto.requests.UpdateApplicationStatusRequest
import com.example.hr_app.data.dto.responses.JobApplicationResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface ApplicationApi {
    @POST("applications")
    suspend fun applyToVacancy(@Body request: CreateJobApplicationRequest): JobApplicationResponse

    @GET("applications/my")
    suspend fun getMyApplications(): List<JobApplicationResponse>

    @GET("applications/vacancy/{vacancyId}")
    suspend fun getApplicationsByVacancy(
        @Path("vacancyId") vacancyId: String
    ): List<JobApplicationResponse>

    @PUT("applications/{id}/status")
    suspend fun updateApplicationStatus(
        @Path("id") id: String,
        @Body request: UpdateApplicationStatusRequest
    ): JobApplicationResponse
}

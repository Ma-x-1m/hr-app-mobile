package com.example.hr_app.data.api

import com.example.hr_app.data.dto.requests.CreateVacancyRequest
import com.example.hr_app.data.dto.requests.UpdateVacancyRequest
import com.example.hr_app.data.dto.responses.VacancyResponse
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface VacancyApi {
    @GET("vacancies")
    suspend fun getAllVacancies(): List<VacancyResponse>

    @GET("vacancies/my")
    suspend fun getMyVacancies(): List<VacancyResponse>

    @GET("vacancies/{id}")
    suspend fun getVacancyById(@Path("id") id: String): VacancyResponse

    @POST("vacancies")
    suspend fun createVacancy(@Body request: CreateVacancyRequest): VacancyResponse

    @PUT("vacancies/{id}")
    suspend fun updateVacancy(
        @Path("id") id: String,
        @Body request: UpdateVacancyRequest
    ): VacancyResponse

    @DELETE("vacancies/{id}")
    suspend fun deleteVacancy(@Path("id") id: String)
}

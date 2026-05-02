package com.example.hr_app.data.api

import com.example.hr_app.data.dto.requests.CreateResumeRequest
import com.example.hr_app.data.dto.requests.UpdateResumeRequest
import com.example.hr_app.data.dto.responses.ResumeResponse
import okhttp3.MultipartBody
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path

interface ResumeApi {
    @GET("resumes/my")
    suspend fun getMyResumes(): List<ResumeResponse>

    @GET("resumes/{id}")
    suspend fun getResumeById(@Path("id") id: String): ResumeResponse

    @POST("resumes")
    suspend fun createResume(@Body request: CreateResumeRequest): ResumeResponse

    @PUT("resumes/{id}")
    suspend fun updateResume(
        @Path("id") id: String,
        @Body request: UpdateResumeRequest
    ): ResumeResponse

    @DELETE("resumes/{id}")
    suspend fun deleteResume(@Path("id") id: String)

    @Multipart
    @POST("resumes/{id}/upload-file")
    suspend fun uploadFile(
        @Path("id") id: String,
        @Part file: MultipartBody.Part
    ): ResumeResponse
}

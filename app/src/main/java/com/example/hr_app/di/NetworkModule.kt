package com.example.hr_app.di

import com.example.hr_app.BuildConfig
import com.example.hr_app.data.api.ApplicationApi
import com.example.hr_app.data.api.AuthApi
import com.example.hr_app.data.api.AuthInterceptor
import com.example.hr_app.data.api.ChatApi
import com.example.hr_app.data.api.ResumeApi
import com.example.hr_app.data.api.VacancyApi
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideOkHttpClient(authInterceptor: AuthInterceptor): OkHttpClient {
        val builder = OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .addInterceptor(authInterceptor)

        if (BuildConfig.DEBUG) {
            val logging = HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            }
            builder.addInterceptor(logging)
        }

        return builder.build()
    }

    @Provides
    @Singleton
    fun provideJson(): Json = Json {
        ignoreUnknownKeys = true
    }

    @Provides
    @Singleton
    fun provideRetrofit(client: OkHttpClient, json: Json): Retrofit =
        Retrofit.Builder()
            .baseUrl("http://10.0.2.2:8080/")
            .client(client)
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            .build()

    @Provides
    @Singleton
    fun provideAuthApi(retrofit: Retrofit): AuthApi =
        retrofit.create(AuthApi::class.java)

    @Provides
    @Singleton
    fun provideVacancyApi(retrofit: Retrofit): VacancyApi =
        retrofit.create(VacancyApi::class.java)

    @Provides
    @Singleton
    fun provideResumeApi(retrofit: Retrofit): ResumeApi =
        retrofit.create(ResumeApi::class.java)

    @Provides
    @Singleton
    fun provideApplicationApi(retrofit: Retrofit): ApplicationApi =
        retrofit.create(ApplicationApi::class.java)

    @Provides
    @Singleton
    fun provideChatApi(retrofit: Retrofit): ChatApi =
        retrofit.create(ChatApi::class.java)
}

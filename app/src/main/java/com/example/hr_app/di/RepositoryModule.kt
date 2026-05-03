package com.example.hr_app.di

import com.example.hr_app.data.repositories.ApplicationRepositoryImpl
import com.example.hr_app.data.repositories.AuthRepositoryImpl
import com.example.hr_app.data.repositories.ChatRepositoryImpl
import com.example.hr_app.data.repositories.ResumeRepositoryImpl
import com.example.hr_app.data.repositories.VacancyRepositoryImpl
import com.example.hr_app.domain.repositories.ApplicationRepository
import com.example.hr_app.domain.repositories.AuthRepository
import com.example.hr_app.domain.repositories.ChatRepository
import com.example.hr_app.domain.repositories.ResumeRepository
import com.example.hr_app.domain.repositories.VacancyRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindAuthRepository(impl: AuthRepositoryImpl): AuthRepository

    @Binds
    @Singleton
    abstract fun bindVacancyRepository(impl: VacancyRepositoryImpl): VacancyRepository

    @Binds
    @Singleton
    abstract fun bindResumeRepository(impl: ResumeRepositoryImpl): ResumeRepository

    @Binds
    @Singleton
    abstract fun bindApplicationRepository(impl: ApplicationRepositoryImpl): ApplicationRepository

    @Binds
    @Singleton
    abstract fun bindChatRepository(impl: ChatRepositoryImpl): ChatRepository
}

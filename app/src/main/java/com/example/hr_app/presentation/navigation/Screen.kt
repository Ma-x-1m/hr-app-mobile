package com.example.hr_app.presentation.navigation

sealed class Screen(val route: String) {

    object Login : Screen("login")
    object Register : Screen("register")

    object VacanciesList : Screen("vacancies_list")

    object VacancyDetail : Screen("vacancy_detail/{vacancyId}") {
        fun createRoute(vacancyId: String) = "vacancy_detail/$vacancyId"
    }

    object MyResumes : Screen("my_resumes")

    object ResumeEdit : Screen("resume_edit?resumeId={resumeId}") {
        fun createRoute(resumeId: String? = null): String =
            if (resumeId != null) "resume_edit?resumeId=$resumeId" else "resume_edit"
    }

    object MyApplications : Screen("my_applications")

    object MyVacancies : Screen("my_vacancies")

    object VacancyEdit : Screen("vacancy_edit?vacancyId={vacancyId}") {
        fun createRoute(vacancyId: String? = null): String =
            if (vacancyId != null) "vacancy_edit?vacancyId=$vacancyId" else "vacancy_edit"
    }

    object VacancyApplications : Screen("vacancy_applications/{vacancyId}") {
        fun createRoute(vacancyId: String) = "vacancy_applications/$vacancyId"
    }

    object Conversations : Screen("conversations")

    object Chat : Screen("chat/{conversationId}") {
        fun createRoute(conversationId: String) = "chat/$conversationId"
    }

    object Profile : Screen("profile")
    object Settings : Screen("settings")
    object ChangePassword : Screen("change_password")
}

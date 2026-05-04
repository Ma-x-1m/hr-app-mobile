package com.example.hr_app.presentation.navigation

sealed class Screen(val route: String) {

    object Login : Screen("login")
    object Register : Screen("register")

    object VacanciesList : Screen("vacancies")

    object VacancyDetail : Screen("vacancy/{id}") {
        fun createRoute(id: String): String = "vacancy/$id"
    }

    object MyResumes : Screen("my_resumes")

    object ResumeEdit : Screen("resume/edit?id={id}") {
        fun createRoute(id: String? = null): String =
            if (id != null) "resume/edit?id=$id" else "resume/edit"
    }

    object MyApplications : Screen("my_applications")

    object MyVacancies : Screen("my_vacancies")

    object VacancyEdit : Screen("vacancy/edit?id={id}") {
        fun createRoute(id: String? = null): String =
            if (id != null) "vacancy/edit?id=$id" else "vacancy/edit"
    }

    object VacancyApplications : Screen("vacancy/{id}/applications") {
        fun createRoute(id: String): String = "vacancy/$id/applications"
    }

    object Conversations : Screen("conversations")

    object ChatScreen : Screen("chat/{conversationId}") {
        fun createRoute(conversationId: String): String = "chat/$conversationId"
    }

    object Profile : Screen("profile")
    object Settings : Screen("settings")
}

package com.example.hr_app.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.hr_app.domain.models.UserRole
import com.example.hr_app.presentation.screens.MainScreen
import com.example.hr_app.presentation.screens.auth.LoginScreen
import com.example.hr_app.presentation.screens.auth.RegisterScreen
import com.example.hr_app.presentation.screens.chat.ChatScreen
import com.example.hr_app.presentation.screens.resumes.ResumeEditScreen
import com.example.hr_app.presentation.screens.settings.ChangePasswordScreen
import com.example.hr_app.presentation.screens.settings.SettingsScreen
import com.example.hr_app.presentation.screens.vacancies.VacancyApplicationsScreen
import com.example.hr_app.presentation.screens.vacancies.VacancyDetailScreen
import com.example.hr_app.presentation.screens.vacancies.VacancyEditScreen

@Composable
fun HrAppNavGraph(
    navController: NavHostController,
    startDestination: String
) {
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable(Screen.Login.route) {
            LoginScreen(navController = navController)
        }

        composable(Screen.Register.route) {
            RegisterScreen(navController = navController)
        }

        // Главные экраны с BottomBar — entry point для залогиненных пользователей
        composable(Screen.VacanciesList.route) {
            MainScreen(rootNavController = navController, userRole = UserRole.APPLICANT)
        }

        composable(Screen.MyVacancies.route) {
            MainScreen(rootNavController = navController, userRole = UserRole.EMPLOYER)
        }

        // Детальные экраны без BottomBar
        composable(
            route = Screen.VacancyDetail.route,
            arguments = listOf(navArgument("vacancyId") { type = NavType.StringType })
        ) { backStack ->
            val id = backStack.arguments?.getString("vacancyId") ?: return@composable
            VacancyDetailScreen(vacancyId = id, navController = navController)
        }

        composable(
            route = Screen.ResumeEdit.route,
            arguments = listOf(
                navArgument("resumeId") {
                    type = NavType.StringType
                    nullable = true
                    defaultValue = null
                }
            )
        ) { backStack ->
            val id = backStack.arguments?.getString("resumeId")
            ResumeEditScreen(resumeId = id, navController = navController)
        }

        composable(
            route = Screen.VacancyEdit.route,
            arguments = listOf(
                navArgument("vacancyId") {
                    type = NavType.StringType
                    nullable = true
                    defaultValue = null
                }
            )
        ) { backStack ->
            val id = backStack.arguments?.getString("vacancyId")
            VacancyEditScreen(vacancyId = id, navController = navController)
        }

        composable(
            route = Screen.VacancyApplications.route,
            arguments = listOf(navArgument("vacancyId") { type = NavType.StringType })
        ) { backStack ->
            val id = backStack.arguments?.getString("vacancyId") ?: return@composable
            VacancyApplicationsScreen(
                vacancyId = id,
                navController = navController
            )
        }

        composable(
            route = Screen.Chat.route,
            arguments = listOf(navArgument("conversationId") { type = NavType.StringType })
        ) { backStack ->
            val id = backStack.arguments?.getString("conversationId") ?: return@composable
            ChatScreen(conversationId = id, navController = navController)
        }

        composable(Screen.Settings.route) {
            SettingsScreen(navController = navController)
        }

        composable(Screen.ChangePassword.route) {
            ChangePasswordScreen(navController = navController)
        }
    }
}

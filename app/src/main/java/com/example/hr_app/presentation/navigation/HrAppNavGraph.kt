package com.example.hr_app.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument

@Composable
fun HrAppNavGraph(
    navController: NavHostController,
    startDestination: String
) {
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable(route = Screen.Login.route) {
            LoginScreen(navController = navController)
        }

        composable(route = Screen.Register.route) {
            RegisterScreen(navController = navController)
        }

        composable(route = Screen.VacanciesList.route) {
            VacanciesListScreen(navController = navController)
        }

        composable(
            route = Screen.VacancyDetail.route,
            arguments = listOf(navArgument("id") { type = NavType.StringType })
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getString("id") ?: return@composable
            VacancyDetailScreen(id = id, navController = navController)
        }

        composable(route = Screen.MyResumes.route) {
            MyResumesScreen(navController = navController)
        }

        composable(
            route = Screen.ResumeEdit.route,
            arguments = listOf(
                navArgument("id") {
                    type = NavType.StringType
                    nullable = true
                    defaultValue = null
                }
            )
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getString("id")
            ResumeEditScreen(id = id, navController = navController)
        }

        composable(route = Screen.MyApplications.route) {
            MyApplicationsScreen(navController = navController)
        }

        composable(route = Screen.MyVacancies.route) {
            MyVacanciesScreen(navController = navController)
        }

        composable(
            route = Screen.VacancyEdit.route,
            arguments = listOf(
                navArgument("id") {
                    type = NavType.StringType
                    nullable = true
                    defaultValue = null
                }
            )
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getString("id")
            VacancyEditScreen(id = id, navController = navController)
        }

        composable(
            route = Screen.VacancyApplications.route,
            arguments = listOf(navArgument("id") { type = NavType.StringType })
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getString("id") ?: return@composable
            VacancyApplicationsScreen(id = id, navController = navController)
        }

        composable(route = Screen.Conversations.route) {
            ConversationsScreen(navController = navController)
        }

        composable(
            route = Screen.ChatScreen.route,
            arguments = listOf(navArgument("conversationId") { type = NavType.StringType })
        ) { backStackEntry ->
            val conversationId = backStackEntry.arguments?.getString("conversationId")
                ?: return@composable
            ChatScreen(conversationId = conversationId, navController = navController)
        }

        composable(route = Screen.Profile.route) {
            ProfileScreen(navController = navController)
        }

        composable(route = Screen.Settings.route) {
            SettingsScreen(navController = navController)
        }
    }
}

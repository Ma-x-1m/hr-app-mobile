package com.example.hr_app.presentation.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.hr_app.domain.models.UserRole
import com.example.hr_app.presentation.components.ApplicantBottomBar
import com.example.hr_app.presentation.components.EmployerBottomBar
import com.example.hr_app.presentation.screens.applications.MyApplicationsScreen
import com.example.hr_app.presentation.screens.chat.ConversationsScreen
import com.example.hr_app.presentation.screens.profile.ProfileScreen
import com.example.hr_app.presentation.screens.resumes.MyResumesScreen
import com.example.hr_app.presentation.screens.vacancies.MyVacanciesScreen
import com.example.hr_app.presentation.screens.vacancies.VacanciesListScreen

@Composable
fun MainScreen(
    rootNavController: NavHostController,
    userRole: UserRole
) {
    val tabNavController = rememberNavController()
    val currentBackStackEntry by tabNavController.currentBackStackEntryAsState()
    val currentRoute = currentBackStackEntry?.destination?.route

    Scaffold(
        bottomBar = {
            if (userRole == UserRole.APPLICANT) {
                ApplicantBottomBar(
                    navController = tabNavController,
                    currentRoute = currentRoute
                )
            } else {
                EmployerBottomBar(
                    navController = tabNavController,
                    currentRoute = currentRoute
                )
            }
        }
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues)) {
            if (userRole == UserRole.APPLICANT) {
                ApplicantTabNavHost(
                    tabNavController = tabNavController,
                    rootNavController = rootNavController
                )
            } else {
                EmployerTabNavHost(
                    tabNavController = tabNavController,
                    rootNavController = rootNavController
                )
            }
        }
    }
}

@Composable
fun ApplicantTabNavHost(
    tabNavController: NavHostController,
    rootNavController: NavHostController
) {
    NavHost(
        navController = tabNavController,
        startDestination = TAB_VACANCIES
    ) {
        composable(TAB_VACANCIES) {
            VacanciesListScreen(navController = rootNavController)
        }
        composable(TAB_RESUMES) {
            MyResumesScreen(navController = rootNavController)
        }
        composable(TAB_APPLICATIONS) {
            MyApplicationsScreen(navController = rootNavController)
        }
        composable(TAB_CONVERSATIONS) {
            ConversationsScreen(navController = rootNavController)
        }
        composable(TAB_PROFILE) {
            ProfileScreen(navController = rootNavController)
        }
    }
}

@Composable
fun EmployerTabNavHost(
    tabNavController: NavHostController,
    rootNavController: NavHostController
) {
    NavHost(
        navController = tabNavController,
        startDestination = TAB_MY_VACANCIES
    ) {
        composable(TAB_MY_VACANCIES) {
            MyVacanciesScreen(navController = rootNavController)
        }
        composable(TAB_CONVERSATIONS) {
            ConversationsScreen(navController = rootNavController)
        }
        composable(TAB_PROFILE) {
            ProfileScreen(navController = rootNavController)
        }
    }
}

const val TAB_VACANCIES = "tab_vacancies"
const val TAB_RESUMES = "tab_resumes"
const val TAB_APPLICATIONS = "tab_applications"
const val TAB_CONVERSATIONS = "tab_conversations"
const val TAB_PROFILE = "tab_profile"
const val TAB_MY_VACANCIES = "tab_my_vacancies"

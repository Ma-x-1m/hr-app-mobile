package com.example.hr_app.presentation.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Chat
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavHostController
import com.example.hr_app.presentation.screens.TAB_APPLICATIONS
import com.example.hr_app.presentation.screens.TAB_CONVERSATIONS
import com.example.hr_app.presentation.screens.TAB_PROFILE
import com.example.hr_app.presentation.screens.TAB_RESUMES
import com.example.hr_app.presentation.screens.TAB_VACANCIES

private data class ApplicantBottomBarItem(
    val route: String,
    val label: String,
    val icon: ImageVector
)

private val applicantItems = listOf(
    ApplicantBottomBarItem(TAB_VACANCIES, "Вакансии", Icons.Default.Search),
    ApplicantBottomBarItem(TAB_RESUMES, "Резюме", Icons.Default.Description),
    ApplicantBottomBarItem(TAB_APPLICATIONS, "Отклики", Icons.AutoMirrored.Filled.Send),
    ApplicantBottomBarItem(TAB_CONVERSATIONS, "Чаты", Icons.AutoMirrored.Filled.Chat),
    ApplicantBottomBarItem(TAB_PROFILE, "Профиль", Icons.Default.Person)
)

@Composable
fun ApplicantBottomBar(
    navController: NavHostController,
    currentRoute: String?
) {
    NavigationBar {
        applicantItems.forEach { item ->
            NavigationBarItem(
                selected = currentRoute == item.route,
                onClick = {
                    if (currentRoute == item.route) return@NavigationBarItem
                    navController.navigate(item.route) {
                        popUpTo(navController.graph.startDestinationId) { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                icon = { Icon(item.icon, contentDescription = item.label) },
                label = { Text(item.label) }
            )
        }
    }
}

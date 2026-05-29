package com.example.hr_app.presentation.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Chat
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Work
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavHostController
import com.example.hr_app.presentation.screens.TAB_CONVERSATIONS
import com.example.hr_app.presentation.screens.TAB_MY_VACANCIES
import com.example.hr_app.presentation.screens.TAB_PROFILE

private data class EmployerBottomBarItem(
    val route: String,
    val label: String,
    val icon: ImageVector
)

private val employerItems = listOf(
    EmployerBottomBarItem(TAB_MY_VACANCIES, "Мои вакансии", Icons.Default.Work),
    EmployerBottomBarItem(TAB_CONVERSATIONS, "Чаты", Icons.AutoMirrored.Filled.Chat),
    EmployerBottomBarItem(TAB_PROFILE, "Профиль", Icons.Default.Person)
)

@Composable
fun EmployerBottomBar(
    navController: NavHostController,
    currentRoute: String?
) {
    NavigationBar {
        employerItems.forEach { item ->
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

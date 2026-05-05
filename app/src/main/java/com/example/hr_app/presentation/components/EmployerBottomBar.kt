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
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import com.example.hr_app.presentation.navigation.Screen

private data class EmployerBottomBarItem(
    val route: String,
    val label: String,
    val icon: ImageVector
)

private val employerItems = listOf(
    EmployerBottomBarItem(Screen.MyVacancies.route, "Мои вакансии", Icons.Default.Work),
    EmployerBottomBarItem(Screen.Conversations.route, "Чаты", Icons.AutoMirrored.Filled.Chat),
    EmployerBottomBarItem(Screen.Profile.route, "Профиль", Icons.Default.Person)
)

@Composable
fun EmployerBottomBar(
    navController: NavController,
    currentRoute: String?
) {
    NavigationBar {
        employerItems.forEach { item ->
            NavigationBarItem(
                selected = currentRoute == item.route,
                onClick = {
                    if (currentRoute == item.route) return@NavigationBarItem
                    navController.navigate(item.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
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

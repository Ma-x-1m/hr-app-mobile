package com.example.hr_app.presentation.screens.vacancies

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.hr_app.domain.models.Vacancy
import com.example.hr_app.presentation.components.EmployerBottomBar
import com.example.hr_app.presentation.components.VacancyCard
import com.example.hr_app.presentation.navigation.Screen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyVacanciesScreen(
    navController: NavController,
    viewModel: MyVacanciesViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route
    var vacancyToDelete by remember { mutableStateOf<Vacancy?>(null) }
    var menuExpandedForId by remember { mutableStateOf<String?>(null) }

    vacancyToDelete?.let { vacancy ->
        AlertDialog(
            onDismissRequest = { vacancyToDelete = null },
            title = { Text("Удалить вакансию?") },
            text = { Text("Вакансия «${vacancy.title}» будет удалена безвозвратно.") },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.deleteVacancy(vacancy.id)
                        vacancyToDelete = null
                    }
                ) {
                    Text("Удалить")
                }
            },
            dismissButton = {
                TextButton(onClick = { vacancyToDelete = null }) {
                    Text("Отмена")
                }
            }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Мои вакансии") })
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navController.navigate(Screen.VacancyEdit.createRoute()) }
            ) {
                Icon(Icons.Default.Add, contentDescription = "Создать вакансию")
            }
        },
        bottomBar = {
            EmployerBottomBar(
                navController = navController,
                currentRoute = currentRoute
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            when {
                uiState.isLoading && uiState.vacancies.isEmpty() && uiState.error == null -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }

                uiState.error != null && uiState.vacancies.isEmpty() -> {
                    ErrorContent(
                        message = uiState.error.orEmpty(),
                        onRetry = { viewModel.loadMyVacancies() },
                        modifier = Modifier.align(Alignment.Center)
                    )
                }

                uiState.vacancies.isEmpty() && !uiState.isLoading -> {
                    Text(
                        text = "У вас пока нет вакансий",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier
                            .align(Alignment.Center)
                            .padding(16.dp)
                    )
                }

                else -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = androidx.compose.foundation.layout.PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        if (uiState.error != null) {
                            item {
                                ErrorContent(
                                    message = uiState.error.orEmpty(),
                                    onRetry = { viewModel.loadMyVacancies() }
                                )
                            }
                        }

                        items(
                            items = uiState.vacancies,
                            key = { it.id }
                        ) { vacancy ->
                            EmployerVacancyRow(
                                vacancy = vacancy,
                                menuExpanded = menuExpandedForId == vacancy.id,
                                onMenuExpand = { menuExpandedForId = vacancy.id },
                                onMenuDismiss = { menuExpandedForId = null },
                                onCardClick = {
                                    navController.navigate(
                                        Screen.VacancyApplications.createRoute(vacancy.id)
                                    )
                                },
                                onEdit = {
                                    menuExpandedForId = null
                                    navController.navigate(
                                        Screen.VacancyEdit.createRoute(vacancy.id)
                                    )
                                },
                                onDelete = {
                                    menuExpandedForId = null
                                    vacancyToDelete = vacancy
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun EmployerVacancyRow(
    vacancy: Vacancy,
    menuExpanded: Boolean,
    onMenuExpand: () -> Unit,
    onMenuDismiss: () -> Unit,
    onCardClick: () -> Unit,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    Box(modifier = Modifier.fillMaxWidth()) {
        VacancyCard(
            vacancy = vacancy,
            onClick = onCardClick
        )
        Box(modifier = Modifier.align(Alignment.TopEnd)) {
            IconButton(onClick = onMenuExpand) {
                Icon(Icons.Default.MoreVert, contentDescription = "Меню")
            }
            DropdownMenu(
                expanded = menuExpanded,
                onDismissRequest = onMenuDismiss
            ) {
                DropdownMenuItem(
                    text = { Text("Редактировать") },
                    onClick = onEdit
                )
                DropdownMenuItem(
                    text = { Text("Удалить") },
                    onClick = onDelete
                )
            }
        }
    }
}

@Composable
private fun ErrorContent(
    message: String,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = message,
            color = MaterialTheme.colorScheme.error,
            style = MaterialTheme.typography.bodyMedium
        )
        Button(onClick = onRetry) {
            Text("Повторить")
        }
    }
}

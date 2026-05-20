package com.example.hr_app.presentation.screens.resumes

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
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
import com.example.hr_app.domain.models.Resume
import com.example.hr_app.presentation.components.ApplicantBottomBar
import com.example.hr_app.presentation.components.ResumeCard
import com.example.hr_app.presentation.navigation.Screen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyResumesScreen(
    navController: NavController,
    viewModel: ResumesViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route
    var resumeToDelete by remember { mutableStateOf<Resume?>(null) }

    resumeToDelete?.let { resume ->
        AlertDialog(
            onDismissRequest = { resumeToDelete = null },
            title = { Text("Удалить резюме?") },
            text = { Text("Резюме «${resume.title}» будет удалено безвозвратно.") },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.deleteResume(resume.id)
                        resumeToDelete = null
                    }
                ) {
                    Text("Удалить")
                }
            },
            dismissButton = {
                TextButton(onClick = { resumeToDelete = null }) {
                    Text("Отмена")
                }
            }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Мои резюме") })
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navController.navigate(Screen.ResumeEdit.createRoute()) }
            ) {
                Icon(Icons.Default.Add, contentDescription = "Создать резюме")
            }
        },
        bottomBar = {
            ApplicantBottomBar(
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
                uiState.isLoading && uiState.resumes.isEmpty() && uiState.error == null -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }

                uiState.error != null && uiState.resumes.isEmpty() -> {
                    ErrorContent(
                        message = uiState.error.orEmpty(),
                        onRetry = { viewModel.loadResumes() },
                        modifier = Modifier.align(Alignment.Center)
                    )
                }

                uiState.resumes.isEmpty() && !uiState.isLoading -> {
                    Text(
                        text = "У вас пока нет резюме",
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
                                    onRetry = { viewModel.loadResumes() }
                                )
                            }
                        }

                        items(
                            items = uiState.resumes,
                            key = { it.id }
                        ) { resume ->
                            ResumeCard(
                                resume = resume,
                                onEdit = {
                                    navController.navigate(
                                        Screen.ResumeEdit.createRoute(resume.id)
                                    )
                                },
                                onDelete = { resumeToDelete = resume },
                                onToggleVisibility = { viewModel.toggleVisibility(resume) }
                            )
                        }
                    }
                }
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

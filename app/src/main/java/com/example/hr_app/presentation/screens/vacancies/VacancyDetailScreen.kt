package com.example.hr_app.presentation.screens.vacancies

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.hr_app.presentation.screens.applications.ApplicationUiState
import com.example.hr_app.presentation.screens.applications.ApplicationViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VacancyDetailScreen(
    id: String,
    navController: NavController,
    viewModel: VacanciesViewModel = hiltViewModel(),
    applicationViewModel: ApplicationViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val applicationUiState by applicationViewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    var showDialog by remember { mutableStateOf(false) }
    var selectedResumeId by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(id) {
        viewModel.loadVacancyById(id)
    }

    LaunchedEffect(showDialog) {
        if (showDialog) {
            selectedResumeId = null
            applicationViewModel.loadResumes()
        }
    }

    LaunchedEffect(applicationUiState.applySuccess) {
        if (applicationUiState.applySuccess) {
            showDialog = false
            snackbarHostState.showSnackbar("Отклик отправлен")
            applicationViewModel.clearApplySuccess()
        }
    }

    val vacancy = uiState.selectedVacancy

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = { Text("Вакансия") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Назад"
                        )
                    }
                }
            )
        },
        bottomBar = {
            if (vacancy != null) {
                Button(
                    onClick = { showDialog = true },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Text("Откликнуться")
                }
            }
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            when {
                uiState.isLoading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }

                uiState.error != null && vacancy == null -> {
                    Text(
                        text = uiState.error.orEmpty(),
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier
                            .align(Alignment.Center)
                            .padding(16.dp)
                    )
                }

                vacancy != null -> {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState())
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Text(
                            text = vacancy.title,
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Bold
                        )

                        if (vacancy.salaryFrom != null && vacancy.salaryTo != null) {
                            Text(
                                text = "${vacancy.salaryFrom} - ${vacancy.salaryTo} ₽",
                                style = MaterialTheme.typography.titleMedium
                            )
                        }

                        val locationParts = listOfNotNull(
                            vacancy.city?.takeIf { it.isNotBlank() },
                            vacancy.experience?.takeIf { it.isNotBlank() }
                        )
                        if (locationParts.isNotEmpty()) {
                            Text(
                                text = locationParts.joinToString(" · "),
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }

                        vacancy.description?.takeIf { it.isNotBlank() }?.let { description ->
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                            ) {
                                Column(modifier = Modifier.padding(16.dp)) {
                                    Text(
                                        text = "Описание",
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                    Text(
                                        text = description,
                                        style = MaterialTheme.typography.bodyMedium,
                                        modifier = Modifier.padding(top = 8.dp)
                                    )
                                }
                            }
                        }

                        vacancy.requirements?.takeIf { it.isNotBlank() }?.let { requirements ->
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                            ) {
                                Column(modifier = Modifier.padding(16.dp)) {
                                    Text(
                                        text = "Требования",
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                    Text(
                                        text = requirements,
                                        style = MaterialTheme.typography.bodyMedium,
                                        modifier = Modifier.padding(top = 8.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    if (showDialog) {
        ApplyDialog(
            applicationUiState = applicationUiState,
            selectedResumeId = selectedResumeId,
            onSelectResume = { selectedResumeId = it },
            onDismiss = { showDialog = false },
            onApply = {
                val resumeId = selectedResumeId ?: return@ApplyDialog
                applicationViewModel.apply(resumeId, id)
            }
        )
    }
}

@Composable
private fun ApplyDialog(
    applicationUiState: ApplicationUiState,
    selectedResumeId: String?,
    onSelectResume: (String) -> Unit,
    onDismiss: () -> Unit,
    onApply: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Выберите резюме") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                when {
                    applicationUiState.resumesLoading -> {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 16.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    }

                    applicationUiState.resumesError != null -> {
                        Text(
                            text = applicationUiState.resumesError.orEmpty(),
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }

                    applicationUiState.resumes.isEmpty() -> {
                        Text(
                            text = "У вас пока нет резюме",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }

                    else -> {
                        applicationUiState.resumes.forEach { resume ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .selectable(
                                        selected = selectedResumeId == resume.id,
                                        onClick = { onSelectResume(resume.id) },
                                        role = Role.RadioButton
                                    )
                                    .padding(vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                RadioButton(
                                    selected = selectedResumeId == resume.id,
                                    onClick = null
                                )
                                Text(
                                    text = resume.title,
                                    style = MaterialTheme.typography.bodyLarge,
                                    modifier = Modifier.padding(start = 8.dp)
                                )
                            }
                        }
                    }
                }

                applicationUiState.applyError?.let { error ->
                    Text(
                        text = error,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }
            }
        },
        confirmButton = {
            Button(
                onClick = onApply,
                enabled = selectedResumeId != null &&
                    !applicationUiState.isApplying &&
                    applicationUiState.resumes.isNotEmpty()
            ) {
                if (applicationUiState.isApplying) {
                    CircularProgressIndicator(
                        modifier = Modifier.padding(horizontal = 8.dp),
                        strokeWidth = 2.dp
                    )
                } else {
                    Text("Отправить отклик")
                }
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Отмена")
            }
        }
    )
}

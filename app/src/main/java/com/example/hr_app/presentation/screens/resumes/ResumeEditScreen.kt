package com.example.hr_app.presentation.screens.resumes

import android.net.Uri
import android.provider.OpenableColumns
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.hr_app.domain.models.ResumeStatus

private val statusOptions = listOf("active", "hidden")

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ResumeEditScreen(
    resumeId: String?,
    navController: NavHostController,
    viewModel: ResumeEditViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    var title by remember { mutableStateOf("") }
    var skills by remember { mutableStateOf("") }
    var experience by remember { mutableStateOf("") }
    var desiredSalaryText by remember { mutableStateOf("") }
    var status by remember { mutableStateOf(statusOptions.first()) }
    var titleError by remember { mutableStateOf(false) }
    var statusExpanded by remember { mutableStateOf(false) }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        uri?.let {
            val fileName = context.contentResolver.query(uri, null, null, null, null)
                ?.use { cursor ->
                    cursor.moveToFirst()
                    cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME))
                } ?: "resume.pdf"
            viewModel.onFileSelected(uri, fileName)
        }
    }

    LaunchedEffect(resumeId) {
        if (resumeId != null) {
            viewModel.loadResume(resumeId)
        }
    }

    LaunchedEffect(uiState.resume) {
        uiState.resume?.let { resume ->
            title = resume.title
            skills = resume.skills.orEmpty()
            experience = resume.experience.orEmpty()
            desiredSalaryText = resume.desiredSalary?.toString().orEmpty()
            status = when (resume.status) {
                ResumeStatus.ACTIVE -> "active"
                ResumeStatus.HIDDEN -> "hidden"
            }
        }
    }

    LaunchedEffect(uiState.isSaved) {
        if (uiState.isSaved) {
            viewModel.clearSaved()
            navController.popBackStack()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (resumeId != null) "Редактирование" else "Новое резюме") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Назад"
                        )
                    }
                }
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            when {
                uiState.isLoading && uiState.resume == null && resumeId != null -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }

                uiState.error != null && uiState.resume == null && resumeId != null -> {
                    Text(
                        text = uiState.error.orEmpty(),
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier
                            .align(Alignment.Center)
                            .padding(16.dp)
                    )
                }

                else -> {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState())
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        if (uiState.isUploadingFile) {
                            LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
                        }

                        OutlinedTextField(
                            value = title,
                            onValueChange = {
                                title = it
                                if (titleError && it.isNotBlank()) titleError = false
                            },
                            label = { Text("Название") },
                            isError = titleError,
                            supportingText = if (titleError) {
                                { Text("Название обязательно") }
                            } else {
                                null
                            },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth()
                        )

                        OutlinedTextField(
                            value = skills,
                            onValueChange = { skills = it },
                            label = { Text("Навыки") },
                            minLines = 3,
                            modifier = Modifier.fillMaxWidth()
                        )

                        OutlinedTextField(
                            value = experience,
                            onValueChange = { experience = it },
                            label = { Text("Опыт") },
                            minLines = 3,
                            modifier = Modifier.fillMaxWidth()
                        )

                        OutlinedTextField(
                            value = desiredSalaryText,
                            onValueChange = { desiredSalaryText = it.filter { c -> c.isDigit() } },
                            label = { Text("Желаемая зарплата") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth()
                        )

                        ExposedDropdownMenuBox(
                            expanded = statusExpanded,
                            onExpandedChange = { statusExpanded = it }
                        ) {
                            OutlinedTextField(
                                value = status,
                                onValueChange = {},
                                readOnly = true,
                                label = { Text("Статус") },
                                trailingIcon = {
                                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = statusExpanded)
                                },
                                modifier = Modifier
                                    .menuAnchor()
                                    .fillMaxWidth()
                            )
                            DropdownMenu(
                                expanded = statusExpanded,
                                onDismissRequest = { statusExpanded = false }
                            ) {
                                statusOptions.forEach { option ->
                                    DropdownMenuItem(
                                        text = { Text(option) },
                                        onClick = {
                                            status = option
                                            statusExpanded = false
                                        }
                                    )
                                }
                            }
                        }

                        Button(
                            onClick = { launcher.launch("application/pdf") },
                            enabled = !uiState.isLoading && !uiState.isUploadingFile,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Прикрепить PDF")
                        }

                        uiState.selectedFileName?.let { fileName ->
                            Text(
                                text = fileName,
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }

                        if (uiState.error != null) {
                            Text(
                                text = uiState.error.orEmpty(),
                                color = MaterialTheme.colorScheme.error,
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }

                        Button(
                            onClick = {
                                if (title.isBlank()) {
                                    titleError = true
                                    return@Button
                                }
                                viewModel.saveResume(
                                    title = title.trim(),
                                    skills = skills.takeIf { it.isNotBlank() },
                                    experience = experience.takeIf { it.isNotBlank() },
                                    desiredSalary = desiredSalaryText.toIntOrNull(),
                                    status = parseResumeStatus(status)
                                )
                            },
                            enabled = !uiState.isLoading && !uiState.isUploadingFile,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            if (uiState.isLoading && !uiState.isUploadingFile) {
                                CircularProgressIndicator(
                                    modifier = Modifier.padding(vertical = 4.dp),
                                    strokeWidth = 2.dp
                                )
                            } else {
                                Text("Сохранить")
                            }
                        }
                    }
                }
            }
        }
    }
}

private fun parseResumeStatus(value: String): ResumeStatus = when (value.lowercase()) {
    "hidden" -> ResumeStatus.HIDDEN
    else -> ResumeStatus.ACTIVE
}

package com.example.hr_app.presentation.screens.vacancies

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.hr_app.domain.models.VacancyStatus

private val experienceOptions = listOf("0-1", "1-3", "3-6", "6+")
private val statusOptions = listOf("open", "closed", "draft")

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VacancyEditScreen(
    vacancyId: String?,
    navController: NavController,
    viewModel: VacancyEditViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var salaryFromText by remember { mutableStateOf("") }
    var salaryToText by remember { mutableStateOf("") }
    var requirements by remember { mutableStateOf("") }
    var city by remember { mutableStateOf("") }
    var experience by remember { mutableStateOf(experienceOptions.first()) }
    var status by remember { mutableStateOf(statusOptions.first()) }
    var titleError by remember { mutableStateOf(false) }

    var experienceExpanded by remember { mutableStateOf(false) }
    var statusExpanded by remember { mutableStateOf(false) }

    LaunchedEffect(vacancyId) {
        if (vacancyId != null) {
            viewModel.loadVacancy(vacancyId)
        }
    }

    LaunchedEffect(uiState.vacancy) {
        uiState.vacancy?.let { vacancy ->
            title = vacancy.title
            description = vacancy.description.orEmpty()
            salaryFromText = vacancy.salaryFrom?.toString().orEmpty()
            salaryToText = vacancy.salaryTo?.toString().orEmpty()
            requirements = vacancy.requirements.orEmpty()
            city = vacancy.city.orEmpty()
            experience = vacancy.experience?.takeIf { it in experienceOptions }
                ?: experienceOptions.first()
            status = vacancy.status.value
        }
    }

    LaunchedEffect(uiState.isSaved) {
        if (uiState.isSaved) {
            navController.popBackStack()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (vacancyId != null) "Редактирование" else "Новая вакансия") },
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
                uiState.isLoading && uiState.vacancy == null && vacancyId != null -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }

                uiState.error != null && uiState.vacancy == null && vacancyId != null -> {
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
                            value = description,
                            onValueChange = { description = it },
                            label = { Text("Описание") },
                            minLines = 3,
                            modifier = Modifier.fillMaxWidth()
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            OutlinedTextField(
                                value = salaryFromText,
                                onValueChange = { salaryFromText = it.filter { c -> c.isDigit() } },
                                label = { Text("Зарплата от") },
                                keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(
                                    keyboardType = KeyboardType.Number
                                ),
                                singleLine = true,
                                modifier = Modifier.weight(1f)
                            )
                            OutlinedTextField(
                                value = salaryToText,
                                onValueChange = { salaryToText = it.filter { c -> c.isDigit() } },
                                label = { Text("Зарплата до") },
                                keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(
                                    keyboardType = KeyboardType.Number
                                ),
                                singleLine = true,
                                modifier = Modifier.weight(1f)
                            )
                        }

                        OutlinedTextField(
                            value = requirements,
                            onValueChange = { requirements = it },
                            label = { Text("Требования") },
                            minLines = 3,
                            modifier = Modifier.fillMaxWidth()
                        )

                        OutlinedTextField(
                            value = city,
                            onValueChange = { city = it },
                            label = { Text("Город") },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth()
                        )

                        ExposedDropdownMenuBox(
                            expanded = experienceExpanded,
                            onExpandedChange = { experienceExpanded = it }
                        ) {
                            OutlinedTextField(
                                value = experience,
                                onValueChange = {},
                                readOnly = true,
                                label = { Text("Опыт") },
                                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = experienceExpanded) },
                                modifier = Modifier
                                    .menuAnchor()
                                    .fillMaxWidth()
                            )
                            DropdownMenu(
                                expanded = experienceExpanded,
                                onDismissRequest = { experienceExpanded = false }
                            ) {
                                experienceOptions.forEach { option ->
                                    DropdownMenuItem(
                                        text = { Text(option) },
                                        onClick = {
                                            experience = option
                                            experienceExpanded = false
                                        }
                                    )
                                }
                            }
                        }

                        ExposedDropdownMenuBox(
                            expanded = statusExpanded,
                            onExpandedChange = { statusExpanded = it }
                        ) {
                            OutlinedTextField(
                                value = status,
                                onValueChange = {},
                                readOnly = true,
                                label = { Text("Статус") },
                                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = statusExpanded) },
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
                                viewModel.saveVacancy(
                                    title = title.trim(),
                                    description = description.takeIf { it.isNotBlank() },
                                    salaryFrom = salaryFromText.toIntOrNull(),
                                    salaryTo = salaryToText.toIntOrNull(),
                                    requirements = requirements.takeIf { it.isNotBlank() },
                                    city = city.takeIf { it.isNotBlank() },
                                    experience = experience,
                                    status = VacancyStatus.fromString(status)
                                )
                            },
                            enabled = !uiState.isLoading,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            if (uiState.isLoading) {
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

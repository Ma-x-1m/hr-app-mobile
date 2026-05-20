package com.example.hr_app.presentation.screens.settings

import androidx.compose.foundation.clickable
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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.hr_app.data.local.ThemeMode
import com.example.hr_app.presentation.navigation.Screen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    navController: NavController,
    viewModel: SettingsViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    var showThemeDialog by remember { mutableStateOf(false) }
    var showAboutDialog by remember { mutableStateOf(false) }
    var deleteConfirmStep by remember { mutableStateOf(0) }

    LaunchedEffect(uiState.isDeleted) {
        if (uiState.isDeleted) {
            navController.navigate(Screen.Login.route) {
                popUpTo(0) { inclusive = true }
            }
        }
    }

    if (showThemeDialog) {
        ThemePickerDialog(
            currentTheme = uiState.currentTheme,
            onDismiss = { showThemeDialog = false },
            onSelect = { mode ->
                viewModel.setTheme(mode)
                showThemeDialog = false
            }
        )
    }

    if (showAboutDialog) {
        AlertDialog(
            onDismissRequest = { showAboutDialog = false },
            title = { Text("О приложении") },
            text = { Text("HR App v1.0.0, Курсовая работа") },
            confirmButton = {
                TextButton(onClick = { showAboutDialog = false }) {
                    Text("OK")
                }
            }
        )
    }

    when (deleteConfirmStep) {
        1 -> {
            AlertDialog(
                onDismissRequest = { deleteConfirmStep = 0 },
                title = { Text("Удалить аккаунт?") },
                text = { Text("Вы уверены?") },
                confirmButton = {
                    TextButton(onClick = { deleteConfirmStep = 2 }) {
                        Text("Далее")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { deleteConfirmStep = 0 }) {
                        Text("Отмена")
                    }
                }
            )
        }
        2 -> {
            AlertDialog(
                onDismissRequest = { deleteConfirmStep = 0 },
                title = { Text("Удаление аккаунта") },
                text = { Text("Это действие необратимо. Продолжить?") },
                confirmButton = {
                    TextButton(
                        onClick = {
                            deleteConfirmStep = 0
                            viewModel.deleteAccount()
                        }
                    ) {
                        Text("Удалить", color = MaterialTheme.colorScheme.error)
                    }
                },
                dismissButton = {
                    TextButton(onClick = { deleteConfirmStep = 0 }) {
                        Text("Отмена")
                    }
                }
            )
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Настройки") },
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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
        ) {
            Text(
                text = "Внешний вид",
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            )
            ListItem(
                headlineContent = { Text("Тема оформления") },
                supportingContent = { Text(themeLabel(uiState.currentTheme)) },
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { showThemeDialog = true }
            )

            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

            Text(
                text = "Аккаунт",
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            )
            ListItem(
                headlineContent = { Text("Сменить пароль") },
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        navController.navigate(Screen.ChangePassword.route)
                    }
            )
            ListItem(
                headlineContent = { Text("О приложении") },
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { showAboutDialog = true }
            )
            ListItem(
                headlineContent = {
                    Text(
                        "Удалить аккаунт",
                        color = MaterialTheme.colorScheme.error
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { deleteConfirmStep = 1 }
            )

            uiState.error?.let { message ->
                Text(
                    text = message,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(16.dp)
                )
            }
        }
    }
}

@Composable
private fun ThemePickerDialog(
    currentTheme: ThemeMode,
    onDismiss: () -> Unit,
    onSelect: (ThemeMode) -> Unit
) {
    val options = listOf(
        ThemeMode.LIGHT to "Светлая",
        ThemeMode.DARK to "Тёмная",
        ThemeMode.SYSTEM to "Системная"
    )

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Тема оформления") },
        text = {
            Column {
                options.forEach { (mode, label) ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .selectable(
                                selected = currentTheme == mode,
                                onClick = { onSelect(mode) },
                                role = Role.RadioButton
                            )
                            .padding(vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = currentTheme == mode,
                            onClick = null
                        )
                        Text(text = label, modifier = Modifier.padding(start = 8.dp))
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Закрыть")
            }
        }
    )
}

private fun themeLabel(mode: ThemeMode): String = when (mode) {
    ThemeMode.LIGHT -> "Светлая"
    ThemeMode.DARK -> "Тёмная"
    ThemeMode.SYSTEM -> "Системная"
}

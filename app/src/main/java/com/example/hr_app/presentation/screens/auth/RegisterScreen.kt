package com.example.hr_app.presentation.screens.auth

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.hr_app.domain.models.UserRole
import com.example.hr_app.presentation.navigation.Screen

@Composable
fun RegisterScreen(
    navController: NavController,
    viewModel: AuthViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordConfirm by remember { mutableStateOf("") }
    var selectedRole by remember { mutableStateOf<UserRole?>(null) }
    var validationError by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(uiState.user) {
        uiState.user?.let { user ->
            val destination = when (user.role) {
                UserRole.APPLICANT -> Screen.VacanciesList.route
                UserRole.EMPLOYER -> Screen.MyVacancies.route
            }
            navController.navigate(destination) {
                popUpTo(0) { inclusive = true }
            }
        }
    }

    val errorText = validationError ?: uiState.error

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Регистрация",
            style = MaterialTheme.typography.displayLarge
        )

        Spacer(modifier = Modifier.height(32.dp))

        OutlinedTextField(
            value = name,
            onValueChange = {
                name = it
                clearErrors(validationError, uiState.error, viewModel) { validationError = null }
            },
            label = { Text("Имя") },
            leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) },
            singleLine = true,
            shape = MaterialTheme.shapes.medium,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = email,
            onValueChange = {
                email = it
                clearErrors(validationError, uiState.error, viewModel) { validationError = null }
            },
            label = { Text("Email") },
            leadingIcon = { Icon(Icons.Default.Email, contentDescription = null) },
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            shape = MaterialTheme.shapes.medium,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = password,
            onValueChange = {
                password = it
                clearErrors(validationError, uiState.error, viewModel) { validationError = null }
            },
            label = { Text("Пароль") },
            leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null) },
            singleLine = true,
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            shape = MaterialTheme.shapes.medium,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = passwordConfirm,
            onValueChange = {
                passwordConfirm = it
                clearErrors(validationError, uiState.error, viewModel) { validationError = null }
            },
            label = { Text("Подтверждение пароля") },
            leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null) },
            singleLine = true,
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            shape = MaterialTheme.shapes.medium,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "Кто вы?",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        RoleOption(
            label = "Я ищу работу",
            selected = selectedRole == UserRole.APPLICANT,
            onClick = {
                selectedRole = UserRole.APPLICANT
                validationError = null
                if (uiState.error != null) viewModel.clearError()
            }
        )
        RoleOption(
            label = "Я работодатель",
            selected = selectedRole == UserRole.EMPLOYER,
            onClick = {
                selectedRole = UserRole.EMPLOYER
                validationError = null
                if (uiState.error != null) viewModel.clearError()
            }
        )

        if (errorText != null) {
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = errorText,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.fillMaxWidth()
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                val error = validate(
                    name = name,
                    email = email,
                    password = password,
                    passwordConfirm = passwordConfirm,
                    role = selectedRole
                )
                if (error != null) {
                    validationError = error
                    return@Button
                }
                validationError = null
                viewModel.register(email.trim(), password, name.trim(), selectedRole!!)
            },
            enabled = !uiState.isLoading,
            modifier = Modifier.fillMaxWidth()
        ) {
            if (uiState.isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.height(20.dp),
                    color = MaterialTheme.colorScheme.onPrimary,
                    strokeWidth = 2.dp
                )
            } else {
                Text("Зарегистрироваться")
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        TextButton(
            onClick = { navController.popBackStack() }
        ) {
            Text("Уже есть аккаунт? Войти")
        }
    }
}

@Composable
private fun RoleOption(
    label: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .selectable(selected = selected, onClick = onClick)
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        RadioButton(selected = selected, onClick = onClick)
        Spacer(modifier = Modifier.height(0.dp))
        Text(text = label, style = MaterialTheme.typography.bodyLarge)
    }
}

private inline fun clearErrors(
    validationError: String?,
    remoteError: String?,
    viewModel: AuthViewModel,
    clearLocal: () -> Unit
) {
    if (validationError != null) clearLocal()
    if (remoteError != null) viewModel.clearError()
}

private fun validate(
    name: String,
    email: String,
    password: String,
    passwordConfirm: String,
    role: UserRole?
): String? = when {
    name.isBlank() -> "Введите имя"
    !email.contains('@') -> "Email должен содержать @"
    password.length < 6 -> "Пароль должен содержать минимум 6 символов"
    password != passwordConfirm -> "Пароли не совпадают"
    role == null -> "Выберите роль"
    else -> null
}

package com.example.hr_app.presentation.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.hr_app.domain.models.Resume
import com.example.hr_app.domain.models.ResumeStatus

@Composable
fun ResumeCard(
    resume: Resume,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
    onToggleVisibility: () -> Unit,
    modifier: Modifier = Modifier
) {
    var menuExpanded by remember { mutableStateOf(false) }

    Card(
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.Top
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = resume.title,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.weight(1f, fill = false)
                    )
                    if (resume.resumeFileUrl != null) {
                        Icon(
                            imageVector = Icons.Default.Description,
                            contentDescription = "PDF прикреплён",
                            modifier = Modifier
                                .padding(start = 8.dp)
                                .size(20.dp),
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }

                resume.desiredSalary?.let { salary ->
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "Желаемая зарплата: ${formatSalary(salary)} ₽",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }

                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = if (resume.status == ResumeStatus.ACTIVE) "Активно" else "Скрыто",
                    style = MaterialTheme.typography.labelLarge,
                    color = if (resume.status == ResumeStatus.ACTIVE) {
                        Color(0xFF2E7D32)
                    } else {
                        MaterialTheme.colorScheme.onSurfaceVariant
                    }
                )
            }

            Box {
                IconButton(onClick = { menuExpanded = true }) {
                    Icon(Icons.Default.MoreVert, contentDescription = "Меню")
                }
                DropdownMenu(
                    expanded = menuExpanded,
                    onDismissRequest = { menuExpanded = false }
                ) {
                    DropdownMenuItem(
                        text = { Text("Редактировать") },
                        onClick = {
                            menuExpanded = false
                            onEdit()
                        }
                    )
                    DropdownMenuItem(
                        text = {
                            Text(
                                if (resume.status == ResumeStatus.ACTIVE) "Скрыть" else "Показать"
                            )
                        },
                        onClick = {
                            menuExpanded = false
                            onToggleVisibility()
                        }
                    )
                    DropdownMenuItem(
                        text = { Text("Удалить") },
                        onClick = {
                            menuExpanded = false
                            onDelete()
                        }
                    )
                }
            }
        }
    }
}

private fun formatSalary(value: Int): String {
    val text = value.toString()
    val sb = StringBuilder()
    val len = text.length
    for (i in 0 until len) {
        if (i > 0 && (len - i) % 3 == 0) sb.append('\u00A0')
        sb.append(text[i])
    }
    return sb.toString()
}

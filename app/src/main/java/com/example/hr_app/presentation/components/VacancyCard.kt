package com.example.hr_app.presentation.components

import android.text.format.DateUtils
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.hr_app.domain.models.Vacancy
import java.text.NumberFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone

private val salaryNumberFormat: NumberFormat =
    NumberFormat.getNumberInstance(Locale("ru", "RU"))

@Composable
fun VacancyCard(
    vacancy: Vacancy,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        onClick = onClick,
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = vacancy.title,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.onSurface
            )

            formatVacancySalary(vacancy.salaryFrom, vacancy.salaryTo)?.let { salary ->
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = salary,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }

            formatVacancyLocation(vacancy.city, vacancy.experience)?.let { location ->
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = location,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = formatRelativeTime(vacancy.createdAt),
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

fun formatVacancySalary(from: Int?, to: Int?): String? {
    if (from == null && to == null) return null
    return when {
        from != null && to != null ->
            "${salaryNumberFormat.format(from)} - ${salaryNumberFormat.format(to)} ₽"
        from != null -> "от ${salaryNumberFormat.format(from)} ₽"
        else -> "до ${salaryNumberFormat.format(to!!)} ₽"
    }
}

fun formatVacancyLocation(city: String?, experience: String?): String? {
    val parts = listOfNotNull(
        city?.takeIf { it.isNotBlank() },
        experience?.takeIf { it.isNotBlank() }
    )
    return if (parts.isEmpty()) null else parts.joinToString(" · ")
}

private fun formatRelativeTime(createdAt: String): String {
    val millis = parseToMillis(createdAt) ?: return createdAt
    return DateUtils.getRelativeTimeSpanString(
        millis,
        System.currentTimeMillis(),
        DateUtils.MINUTE_IN_MILLIS
    ).toString()
}

private val zonedPatterns = listOf(
    "yyyy-MM-dd'T'HH:mm:ss.SSSXXX",
    "yyyy-MM-dd'T'HH:mm:ssXXX"
)

private val utcPatterns = listOf(
    "yyyy-MM-dd'T'HH:mm:ss.SSS",
    "yyyy-MM-dd'T'HH:mm:ss"
)

private fun parseToMillis(value: String): Long? {
    for (pattern in zonedPatterns) {
        try {
            return SimpleDateFormat(pattern, Locale.US).parse(value)?.time
        } catch (_: ParseException) {
        }
    }
    for (pattern in utcPatterns) {
        try {
            val sdf = SimpleDateFormat(pattern, Locale.US).apply {
                timeZone = TimeZone.getTimeZone("UTC")
            }
            return sdf.parse(value)?.time
        } catch (_: ParseException) {
        }
    }
    return null
}

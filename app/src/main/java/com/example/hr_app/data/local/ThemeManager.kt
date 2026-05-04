package com.example.hr_app.data.local

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

sealed class ThemeMode(val value: String) {
    object LIGHT : ThemeMode("light")
    object DARK : ThemeMode("dark")
    object SYSTEM : ThemeMode("system")

    companion object {
        fun fromValue(value: String?): ThemeMode = when (value) {
            LIGHT.value -> LIGHT
            DARK.value -> DARK
            else -> SYSTEM
        }
    }
}

private val Context.themeDataStore by preferencesDataStore(name = "theme_prefs")

@Singleton
class ThemeManager @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val themeKey = stringPreferencesKey("theme_mode")

    suspend fun setTheme(mode: ThemeMode) {
        context.themeDataStore.edit { prefs ->
            prefs[themeKey] = mode.value
        }
    }

    fun getThemeFlow(): Flow<ThemeMode> =
        context.themeDataStore.data.map { ThemeMode.fromValue(it[themeKey]) }
}

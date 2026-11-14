package com.example.nusalingo.data.local

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(name = "settings")
class UserPreferences(private val context: Context) {
    private val languageKey = stringPreferencesKey("selected_language")
    private val categoryKey = stringPreferencesKey("selected_category")
    val language: Flow<String> = context.dataStore.data.map { prefs ->
        prefs[languageKey] ?: "Jawa"
    }
    val category: Flow<String> = context.dataStore.data.map { prefs ->
        prefs[categoryKey] ?: "Aksara Dasar"
    }
    suspend fun saveLanguage(value: String) {
        context.dataStore.edit { prefs -> prefs[languageKey] = value }
    }
    suspend fun saveCategory(value: String) {
        context.dataStore.edit { prefs -> prefs[categoryKey] = value }
    }
}
package com.example.nusalingo.data.local

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(name = "settings")
class UserPreferences(private val context: Context) {
    private val languageKey = stringPreferencesKey("selected_language")
    private val categoryKey = stringPreferencesKey("selected_category")
    private val totalXpKey = intPreferencesKey("total_xp")

    val totalXp: Flow<Int> = context.dataStore.data.map { prefs ->
        prefs[totalXpKey] ?: 0
    }

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

    suspend fun addXp(amount: Int): Int {
        require(amount >= 0) { "XP amount cannot be negative." }

        var updatedTotal = 0
        context.dataStore.edit { prefs ->
            updatedTotal = (prefs[totalXpKey] ?: 0) + amount
            prefs[totalXpKey] = updatedTotal
        }
        return updatedTotal
    }
}
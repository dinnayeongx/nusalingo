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
    private val completedLevelCheckpointsKey = stringPreferencesKey("completed_level_checkpoints")
    private val levelCheckpointScoresKey = stringPreferencesKey("level_checkpoint_scores")

    val totalXp: Flow<Int> = context.dataStore.data.map { prefs -> prefs[totalXpKey] ?: 0 }
    val language: Flow<String> = context.dataStore.data.map { prefs -> prefs[languageKey] ?: "Jawa" }
    val category: Flow<String> = context.dataStore.data.map { prefs -> prefs[categoryKey] ?: "Aksara Dasar" }

    val completedLevelCheckpoints: Flow<Set<String>> = context.dataStore.data.map { prefs ->
        prefs[completedLevelCheckpointsKey]
            .orEmpty()
            .split(',')
            .filter { it.isNotBlank() }
            .toSet()
    }
    val levelCheckpointScores: Flow<Map<String, Int>> = context.dataStore.data.map { prefs ->
        prefs[levelCheckpointScoresKey]
            .orEmpty()
            .split(';')
            .mapNotNull { entry ->
                val parts = entry.split(':')
                if (parts.size != 2) null else parts[0] to (parts[1].toIntOrNull() ?: return@mapNotNull null)
            }.toMap()
    }
    suspend fun saveLanguage(value: String) {
        context.dataStore.edit { prefs -> prefs[languageKey] = value }
    }
    suspend fun saveCategory(value: String) {
        context.dataStore.edit { prefs -> prefs[categoryKey] = value }
    }

    suspend fun clearProgress() {
        context.dataStore.edit { prefs ->
            prefs.remove(totalXpKey)
            prefs.remove(completedLevelCheckpointsKey)
            prefs.remove(levelCheckpointScoresKey)
        }
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
    suspend fun completeLevelCheckpoint(checkpointId: String, score: Int): Boolean {
        require(checkpointId.isNotBlank())
        require(score in 0..100)

        var newlyCompleted = false
        context.dataStore.edit { prefs ->
            val completed = prefs[completedLevelCheckpointsKey].orEmpty()
                .split(',').filter { it.isNotBlank() }.toMutableSet()
            val scoreMap = prefs[levelCheckpointScoresKey].orEmpty()
                .split(';').mapNotNull { entry ->
                    val parts = entry.split(':')
                    if (parts.size == 2) parts[0] to (parts[1].toIntOrNull() ?: 0) else null
                }.toMap().toMutableMap()

            val previousBest = scoreMap[checkpointId] ?: 0
            if (score > previousBest) scoreMap[checkpointId] = score

            if (score >= 80 && completed.add(checkpointId)) {
                newlyCompleted = true
                prefs[totalXpKey] = (prefs[totalXpKey] ?: 0) + score
            }

            prefs[completedLevelCheckpointsKey] = completed.joinToString(",")
            prefs[levelCheckpointScoresKey] = scoreMap.entries.joinToString(";") { "${it.key}:${it.value}" }
        }
        return newlyCompleted
    }
}
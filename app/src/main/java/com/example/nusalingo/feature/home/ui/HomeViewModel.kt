package com.example.nusalingo.feature.home.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.nusalingo.data.LevelQuizConfig
import com.example.nusalingo.data.local.UserPreferences
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class HomeViewModel(application: Application) : AndroidViewModel(application) {
    private val preferences = UserPreferences(application)

    val uiState = combine(
        preferences.language,
        preferences.completedLevelCheckpoints
    ) { language, completed ->
        val levels = LevelQuizConfig.levels.map { level ->
            val levelCheckpoints = LevelQuizConfig.checkpointsForLevel(level.id)
            val completedCount = levelCheckpoints.count { it.id in completed }
            val unlocked = when (level.id) {
                1 -> true
                else -> LevelQuizConfig.checkpointsForLevel(level.id - 1)
                    .all { it.id in completed }
            }
            LearningLevel(
                levelNumber = level.id,
                title = level.title,
                completedCheckpoints = completedCount,
                totalCheckpoints = levelCheckpoints.size,
                isUnlocked = unlocked
            )
        }

        HomeState(
            selectedAksaraName = "Aksara $language",
            levelList = levels,
            aksaraList = listOf(
                AksaraItem("jawa", "Aksara Jawa", true, "3 checkpoint"),
                AksaraItem("madura", "Aksara Madura", false, "Segera hadir"),
                AksaraItem("bali", "Aksara Bali", false, "Segera hadir")
            )
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), HomeState())

    fun selectAksara(item: AksaraItem) {
        if (item.id != "jawa") return

        viewModelScope.launch {
            preferences.saveLanguage("Jawa")
        }
    }
}
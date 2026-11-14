package com.example.nusalingo.feature.lesson.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.nusalingo.data.local.AppDatabase
import com.example.nusalingo.data.local.CharacterEntity
import com.example.nusalingo.data.local.UserPreferences
import com.example.nusalingo.feature.lesson.data.LessonRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class LessonViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = LessonRepository(
        dao = AppDatabase.getDatabase(application).lessonDao(),
        prefs = UserPreferences(application)
    )

    val selectedLanguage: StateFlow<String> = repository.selectedLanguage
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), "Jawa")

    val selectedCategory: StateFlow<String> = repository.selectedCategory
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), "Aksara Dasar")

    val completedLessons: StateFlow<List<String>> = repository.completedLessons
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    init {
        viewModelScope.launch {
            repository.seedIfEmpty()
        }
    }

    fun getCharacters(lesson: String): Flow<List<CharacterEntity>> {
        return repository.getCharacters(lesson)
    }

    fun selectLanguage(language: String) {
        viewModelScope.launch { repository.saveLanguage(language) }
    }

    fun selectCategory(category: String) {
        viewModelScope.launch { repository.saveCategory(category) }
    }

    fun completeLesson(lesson: String, learnedCount: Int) {
        viewModelScope.launch { repository.markCompleted(lesson, learnedCount) }
    }
}
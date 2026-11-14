package com.example.nusalingo.feature.lesson.data

import com.example.nusalingo.data.local.CharacterEntity
import com.example.nusalingo.data.local.HistoryEntity
import com.example.nusalingo.data.local.LessonDao
import com.example.nusalingo.data.local.LessonSeed
import com.example.nusalingo.data.local.UserPreferences
import kotlinx.coroutines.flow.Flow

class LessonRepository(
    private val dao: LessonDao,
    private val prefs: UserPreferences
) {
    val selectedLanguage: Flow<String> = prefs.language
    val selectedCategory: Flow<String> = prefs.category
    suspend fun saveLanguage(value: String) = prefs.saveLanguage(value)
    suspend fun saveCategory(value: String) = prefs.saveCategory(value)
    val completedLessons: Flow<List<String>> = dao.getCompletedLessons()
    fun getCharacters(lesson: String): Flow<List<CharacterEntity>> {
        return dao.getCharacters(lesson)
    }
    suspend fun seedIfEmpty() {
        dao.clearCharacters()
        dao.insertCharacters(LessonSeed.all())
    }
    suspend fun markCompleted(lesson: String, learnedCount: Int) {
        dao.insertHistory(
            HistoryEntity(
                lesson = lesson,
                completedAt = System.currentTimeMillis(),
                learnedCount = learnedCount
            )
        )
    }
}
package com.example.nusalingo.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface LessonDao {
    @Query("SELECT * FROM characters WHERE lesson = :lesson ORDER BY id")
    fun getCharacters(lesson: String): Flow<List<CharacterEntity>>
    @Query("SELECT COUNT(*) FROM characters")
    suspend fun countCharacters(): Int
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCharacters(list: List<CharacterEntity>)
    @Insert
    suspend fun insertHistory(history: HistoryEntity)
    @Query("SELECT DISTINCT lesson FROM history")
    fun getCompletedLessons(): Flow<List<String>>
    @Query("SELECT * FROM history ORDER BY completedAt DESC")
    fun getHistory(): Flow<List<HistoryEntity>>
    @Query("DELETE FROM characters")
    suspend fun clearCharacters()
}
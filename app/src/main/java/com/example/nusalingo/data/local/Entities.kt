package com.example.nusalingo.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "characters")
data class CharacterEntity(
    @PrimaryKey val id: Int,
    val language: String,
    val category: String,
    val lesson: String,
    val section: String,
    val glyph: String,
    val latin: String,
    val audioName: String
)
@Entity(tableName = "history")
data class HistoryEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val lesson: String,
    val completedAt: Long,
    val learnedCount: Int
)
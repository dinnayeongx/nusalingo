package com.example.nusalingo.data.repository

import com.example.nusalingo.data.model.Achievement
import com.example.nusalingo.data.model.ActivityHistory
import com.example.nusalingo.data.model.UserProfile
import com.example.nusalingo.data.model.UserStats
import kotlinx.coroutines.flow.Flow

/**
 * Kontrak data untuk Profile. Nanti implementasi aslinya (Room + Firestore)
 * tinggal dibuat dengan mengimplementasi interface ini, ViewModel tidak perlu diubah.
 */
interface ProfileRepository {
    fun observeProfile(): Flow<UserProfile>
    fun observeStats(): Flow<UserStats>
    fun observeActivities(): Flow<List<ActivityHistory>>
    fun observeAchievements(): Flow<List<Achievement>>
    suspend fun logout()
}

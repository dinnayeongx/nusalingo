package com.example.nusalingo.feature.profile

import com.example.nusalingo.data.model.Achievement
import com.example.nusalingo.data.model.ActivityHistory
import com.example.nusalingo.data.model.UserProfile
import com.example.nusalingo.data.model.UserStats

data class ProfileUiState(
    val isLoading: Boolean = true,
    val profile: UserProfile? = null,
    val stats: UserStats = UserStats(),
    val activities: List<ActivityHistory> = emptyList(),
    val achievements: List<Achievement> = emptyList(),
    val isLoggingOut: Boolean = false,
    val errorMessage: String? = null
)

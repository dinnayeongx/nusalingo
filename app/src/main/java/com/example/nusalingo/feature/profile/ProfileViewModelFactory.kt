package com.example.nusalingo.feature.profile

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.nusalingo.data.local.AppDatabase
import com.example.nusalingo.data.local.UserPreferences
import com.example.nusalingo.data.repository.LocalProfileRepository
import com.example.nusalingo.data.sync.ProgressSyncManager

class ProfileViewModelFactory(
    private val appContext: Context
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val dao = AppDatabase.getDatabase(appContext).lessonDao()
        val preferences = UserPreferences(appContext)
        val repository = LocalProfileRepository(
            lessonDao = dao,
            userPreferences = preferences,
            syncManager = ProgressSyncManager(dao, preferences)
        )
        return ProfileViewModel(repository) as T
    }
}

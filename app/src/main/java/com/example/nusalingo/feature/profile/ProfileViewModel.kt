package com.example.nusalingo.feature.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nusalingo.data.repository.FakeProfileRepository
import com.example.nusalingo.data.repository.ProfileRepository
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val repository: ProfileRepository = FakeProfileRepository()
) : ViewModel() {

    private data class LogoutState(
        val isLoggingOut: Boolean = false,
        val error: String? = null
    )

    private val logoutState = MutableStateFlow(LogoutState())

    val uiState: StateFlow<ProfileUiState> = combine(
        repository.observeProfile(),
        repository.observeStats(),
        repository.observeActivities(),
        repository.observeAchievements(),
        logoutState
    ) { profile, stats, activities, achievements, logout ->
        ProfileUiState(
            isLoading = false,
            profile = profile,
            stats = stats,
            activities = activities,
            achievements = achievements,
            isLoggingOut = logout.isLoggingOut,
            errorMessage = logout.error
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = ProfileUiState()
    )

    /** onDone hanya dipanggil kalau logout berhasil. Kalau gagal, errorMessage terisi. */
    fun logout(onDone: () -> Unit) {
        if (logoutState.value.isLoggingOut) return
        viewModelScope.launch {
            logoutState.value = LogoutState(isLoggingOut = true)
            try {
                repository.logout()
                logoutState.value = LogoutState()
                onDone()
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                logoutState.value = LogoutState(
                    error = e.message ?: "Gagal keluar. Coba lagi."
                )
            }
        }
    }

    fun dismissError() {
        logoutState.update { it.copy(error = null) }
    }
}

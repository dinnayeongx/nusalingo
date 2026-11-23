package com.example.nusalingo.feature.profile

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.nusalingo.R
import com.example.nusalingo.feature.profile.components.ProfileColors

/**
 * Penghubung ViewModel dan UI. Ini yang dipanggil dari NavGraph.
 */
@Composable
fun ProfileRoute(
    onNavigateToAchievement: () -> Unit,
    onLoggedOut: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ProfileViewModel = viewModel(
        factory = ProfileViewModelFactory(LocalContext.current.applicationContext)
    )
) {
    val state by viewModel.uiState.collectAsState()
    val profile = state.profile

    if (state.isLoading || profile == null) {
        Box(
            modifier = modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(color = ProfileColors.Navy)
        }
        return
    }

    ProfileScreen(
        username = profile.username,
        handle = profile.handle,
        joinDate = profile.joinDate,
        avatarRes = R.drawable.ic_launcher_foreground, // ganti dengan gambar buaya
        dayStreak = state.stats.dayStreak,
        totalXp = state.stats.totalXp,
        totalCrown = state.stats.totalCrown,
        level = state.stats.level,
        activities = state.activities,
        isGuest = profile.isGuest,
        isLoggingOut = state.isLoggingOut,
        onEditAvatarClick = { /* TODO */ },
        onAchievementClick = onNavigateToAchievement,
        onThemeClick = { /* TODO */ },
        onReminderClick = { /* TODO */ },
        onNotificationClick = { /* TODO */ },
        onSoundClick = { /* TODO */ },
        onLogoutClick = { viewModel.logout(onLoggedOut) },
        modifier = modifier
    )

    state.errorMessage?.let { message ->
        AlertDialog(
            onDismissRequest = viewModel::dismissError,
            confirmButton = {
                TextButton(onClick = viewModel::dismissError) { Text("Tutup") }
            },
            title = { Text("Gagal keluar") },
            text = { Text(message) }
        )
    }
}

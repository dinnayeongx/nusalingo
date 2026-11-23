package com.example.nusalingo.feature.profile

import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.Stairs
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.nusalingo.R
import com.example.nusalingo.data.model.ActivityHistory
import com.example.nusalingo.data.model.ActivityType
import com.example.nusalingo.feature.profile.components.ActivityHistoryItem
import com.example.nusalingo.feature.profile.components.ProfileColors
import com.example.nusalingo.feature.profile.components.ProfileHeader
import com.example.nusalingo.feature.profile.components.SettingItem
import com.example.nusalingo.feature.profile.components.StatCard

@Composable
fun ProfileScreen(
    username: String,
    handle: String,
    joinDate: String,
    @DrawableRes avatarRes: Int,
    dayStreak: Int,
    totalXp: Int,
    totalCrown: Int,
    level: Int,
    activities: List<ActivityHistory>,
    isGuest: Boolean,
    isLoggingOut: Boolean,
    onEditAvatarClick: () -> Unit,
    onAchievementClick: () -> Unit,
    onThemeClick: () -> Unit,
    onReminderClick: () -> Unit,
    onNotificationClick: () -> Unit,
    onSoundClick: () -> Unit,
    onLogoutClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(ProfileColors.Cream)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp, vertical = 16.dp)
    ) {
        SectionDivider()

        ProfileHeader(
            username = username,
            handle = handle,
            joinDate = joinDate,
            avatarRes = avatarRes,
            onEditAvatarClick = onEditAvatarClick,
            modifier = Modifier.padding(vertical = 12.dp)
        )

        SectionDivider()

        SectionTitle("Statistik")

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            StatCard(
                icon = Icons.Filled.LocalFireDepartment,
                value = dayStreak.toString(),
                label = "Day streak",
                modifier = Modifier.weight(1f)
            )
            StatCard(
                icon = Icons.Filled.Bolt,
                value = totalXp.toString(),
                label = "Total XP",
                modifier = Modifier.weight(1f)
            )
        }
        Spacer(Modifier.height(12.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            StatCard(
                icon = Icons.Filled.EmojiEvents,
                value = totalCrown.toString(),
                label = "Total crown",
                modifier = Modifier.weight(1f)
            )
            StatCard(
                icon = Icons.Filled.Stairs,
                value = level.toString(),
                label = "Level",
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(Modifier.height(20.dp))
        SectionDivider()

        SectionTitle("Pencapaian")
        SettingItem(title = "Lihat Pencapaian", onClick = onAchievementClick)

        Spacer(Modifier.height(20.dp))
        SectionDivider()

        SectionTitle("Riwayat Aktivitas")
        if (activities.isEmpty()) {
            Text(
                text = "Belum ada aktivitas",
                fontSize = 13.sp,
                color = Color.Gray
            )
        } else {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                activities.take(5).forEach { activity ->
                    ActivityHistoryItem(activity)
                }
            }
        }

        Spacer(Modifier.height(20.dp))
        SectionDivider()

        SectionTitle("Pengaturan")

        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            SettingItem(title = "Tema", onClick = onThemeClick)
            SettingItem(title = "Waktu Pengingat", onClick = onReminderClick)
            SettingItem(title = "Notifikasi", onClick = onNotificationClick)
            SettingItem(title = "Efek Suara", onClick = onSoundClick)
            SettingItem(
                title = when {
                    isLoggingOut -> "Menyimpan progres..."
                    isGuest -> "Masuk / Daftar"
                    else -> "Keluar"
                },
                onClick = { if (!isLoggingOut) onLogoutClick() },
                containerColor = if (isGuest) ProfileColors.Navy else Color(0xFFD94B4B),
                showArrow = false
            )
        }

        // Ruang supaya item terakhir tidak tertutup bottom navigation bar
        Spacer(Modifier.height(24.dp))
    }
}

@Composable
private fun SectionTitle(text: String) {
    Text(
        text = text,
        fontSize = 20.sp,
        fontWeight = FontWeight.Bold,
        color = ProfileColors.Navy,
        modifier = Modifier.padding(top = 12.dp, bottom = 12.dp)
    )
}

@Composable
private fun SectionDivider() {
    HorizontalDivider(color = ProfileColors.Divider, thickness = 1.dp)
}

@Preview(showBackground = true, widthDp = 390, heightDp = 844)
@Composable
private fun ProfileScreenPreview() {
    ProfileScreen(
        username = "justinBeibeh",
        handle = "Justbeibeh799",
        joinDate = "September 2026",
        avatarRes = R.drawable.ic_launcher_foreground, // ganti dengan gambar buaya
        dayStreak = 4,
        totalXp = 302,
        totalCrown = 3,
        level = 3,
        activities = listOf(
            ActivityHistory("1", ActivityType.QUIZ, "Quiz Salam dan Sapaan", 90, 30, System.currentTimeMillis()),
            ActivityHistory("2", ActivityType.LESSON, "Perkenalan Diri", null, 20, System.currentTimeMillis())
        ),
        isGuest = false,
        isLoggingOut = false,
        onEditAvatarClick = {},
        onAchievementClick = {},
        onThemeClick = {},
        onReminderClick = {},
        onNotificationClick = {},
        onSoundClick = {},
        onLogoutClick = {}
    )
}

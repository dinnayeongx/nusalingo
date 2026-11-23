package com.example.nusalingo.feature.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.nusalingo.data.model.Achievement
import com.example.nusalingo.feature.profile.components.AchievementItem
import com.example.nusalingo.feature.profile.components.ProfileColors

@Composable
fun AchievementScreen(
    achievements: List<Achievement>,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(ProfileColors.Cream)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBackClick) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Kembali",
                    tint = ProfileColors.Navy
                )
            }
            Text(
                text = "Pencapaian",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = ProfileColors.Navy
            )
        }

        val unlockedCount = achievements.count { it.unlocked }
        Text(
            text = "$unlockedCount dari ${achievements.size} terbuka",
            fontSize = 13.sp,
            color = androidx.compose.ui.graphics.Color.Gray,
            modifier = Modifier.padding(horizontal = 20.dp, vertical = 4.dp)
        )

        LazyColumn(
            contentPadding = androidx.compose.foundation.layout.PaddingValues(20.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(achievements, key = { it.id }) { achievement ->
                AchievementItem(achievement)
            }
        }
    }
}

@Preview(showBackground = true, widthDp = 390, heightDp = 844)
@Composable
private fun AchievementScreenPreview() {
    AchievementScreen(
        achievements = listOf(
            Achievement("a1", "Langkah Pertama", "Selesaikan lesson pertamamu", true, 1, 1),
            Achievement("a2", "Konsisten", "Capai streak 7 hari", false, 4, 7),
            Achievement("a3", "Kolektor XP", "Kumpulkan 500 XP", false, 302, 500)
        ),
        onBackClick = {}
    )
}

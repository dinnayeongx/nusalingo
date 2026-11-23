package com.example.nusalingo.data.repository

import com.example.nusalingo.data.model.Achievement
import com.example.nusalingo.data.model.ActivityHistory
import com.example.nusalingo.data.model.ActivityType
import com.example.nusalingo.data.model.UserProfile
import com.example.nusalingo.data.model.UserStats
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

/** Data dummy untuk mengetes UI. Ganti dengan repository asli kalau database sudah siap. */
class FakeProfileRepository : ProfileRepository {

    private val day = 24L * 60 * 60 * 1000
    private val now = System.currentTimeMillis()

    override fun observeProfile(): Flow<UserProfile> = flowOf(
        UserProfile(
            id = "1",
            username = "justinBeibeh",
            handle = "Justbeibeh799",
            joinDate = "September 2026"
        )
    )

    override fun observeStats(): Flow<UserStats> = flowOf(
        UserStats(dayStreak = 4, totalXp = 302, totalCrown = 3, level = 3)
    )

    override fun observeActivities(): Flow<List<ActivityHistory>> = flowOf(
        listOf(
            ActivityHistory("1", ActivityType.QUIZ, "Quiz Salam dan Sapaan", 90, 30, now),
            ActivityHistory("2", ActivityType.LESSON, "Perkenalan Diri", null, 20, now - day),
            ActivityHistory("3", ActivityType.PRACTICE, "Latihan Angka 1-10", 80, 15, now - 2 * day),
            ActivityHistory("4", ActivityType.LESSON, "Kata Benda Dasar", null, 20, now - 3 * day)
        )
    )

    override fun observeAchievements(): Flow<List<Achievement>> = flowOf(
        listOf(
            Achievement("a1", "Langkah Pertama", "Selesaikan lesson pertamamu", true, 1, 1),
            Achievement("a2", "Konsisten", "Capai streak 7 hari", false, 4, 7),
            Achievement("a3", "Kolektor XP", "Kumpulkan 500 XP", false, 302, 500),
            Achievement("a4", "Juara Quiz", "Dapat skor 100 di 3 quiz", false, 1, 3)
        )
    )

    override suspend fun logout() {
        // TODO: panggil FirebaseAuth.signOut() / fungsi logout dari feature/auth
    }
}

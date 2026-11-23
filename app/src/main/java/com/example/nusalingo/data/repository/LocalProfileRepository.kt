package com.example.nusalingo.data.repository

import com.example.nusalingo.data.LevelQuizConfig
import com.example.nusalingo.data.local.LessonDao
import com.example.nusalingo.data.local.UserPreferences
import com.example.nusalingo.data.model.Achievement
import com.example.nusalingo.data.model.ActivityHistory
import com.example.nusalingo.data.model.ActivityType
import com.example.nusalingo.data.model.UserProfile
import com.example.nusalingo.data.model.UserStats
import com.example.nusalingo.data.sync.ProgressSyncManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

class LocalProfileRepository(
    private val lessonDao: LessonDao,
    private val userPreferences: UserPreferences,
    private val syncManager: ProgressSyncManager
) : ProfileRepository {

    override fun observeProfile(): Flow<UserProfile> = currentUserFlow().map { user ->
        if (user == null) {
            UserProfile(
                id = "guest",
                username = "Tamu",
                handle = "Belum masuk akun",
                joinDate = "",
                isGuest = true
            )
        } else {
            val email = user.email.orEmpty()
            UserProfile(
                id = user.uid,
                username = user.displayName?.takeIf { it.isNotBlank() }
                    ?: email.substringBefore('@').ifBlank { "Pengguna" },
                handle = email,
                joinDate = formatJoinDate(user.metadata?.creationTimestamp ?: 0L),
                isGuest = false
            )
        }
    }

    override fun observeStats(): Flow<UserStats> = combine(
        userPreferences.totalXp,
        userPreferences.completedLevelCheckpoints,
        lessonDao.getHistory()
    ) { xp, completed, history ->
        UserStats(
            dayStreak = currentStreak(history.map { it.completedAt }),
            totalXp = xp,
            totalCrown = completed.count { LevelQuizConfig.checkpoint(it) != null },
            level = unlockedLevel(completed)
        )
    }

    override fun observeActivities(): Flow<List<ActivityHistory>> =
        lessonDao.getHistory().map { list ->
            list.map { h ->
                ActivityHistory(
                    id = "lesson_${h.id}",
                    type = ActivityType.LESSON,
                    title = h.lesson,
                    score = null,
                    xpEarned = null, // lesson tidak memberi XP di data saat ini
                    timestamp = h.completedAt,
                    learnedCount = h.learnedCount
                )
            }
        }

    override fun observeAchievements(): Flow<List<Achievement>> = combine(
        userPreferences.totalXp,
        userPreferences.completedLevelCheckpoints,
        userPreferences.levelCheckpointScores,
        lessonDao.getHistory()
    ) { xp, completed, scores, history ->
        val validCompleted = completed.filter { LevelQuizConfig.checkpoint(it) != null }
        val level1 = LevelQuizConfig.checkpointsForLevel(1)

        listOf(
            achievement("first_lesson", "Langkah Pertama", "Selesaikan lesson pertamamu",
                history.size, 1),
            achievement("first_checkpoint", "Lulus Pertama", "Lulus checkpoint quiz pertama",
                validCompleted.size, 1),
            achievement("streak_7", "Konsisten", "Belajar 7 hari berturut-turut",
                longestStreak(history.map { it.completedAt }), 7),
            achievement("xp_500", "Kolektor XP", "Kumpulkan 500 XP",
                xp, 500),
            achievement("perfect_score", "Nilai Sempurna", "Dapat skor 100 di sebuah checkpoint",
                scores.values.count { it == 100 }, 1),
            achievement("level_1", "Penakluk Level 1", "Lulus semua checkpoint Level 1",
                level1.count { it.id in completed }, level1.size),
            achievement("all_levels", "Tamat Belajar", "Lulus semua checkpoint di semua level",
                validCompleted.size, LevelQuizConfig.checkpoints.size)
        )
    }


    override suspend fun logout() {
        val auth = FirebaseAuth.getInstance()
        val user = auth.currentUser ?: return

        syncManager.pushForLogout(user.uid).getOrThrow()
        auth.signOut()
        syncManager.clearLocalProgress()
    }

    // ---------- akun ----------

    private fun currentUserFlow(): Flow<FirebaseUser?> = callbackFlow {
        val auth = FirebaseAuth.getInstance()
        val listener = FirebaseAuth.AuthStateListener { trySend(it.currentUser) }
        auth.addAuthStateListener(listener)
        awaitClose { auth.removeAuthStateListener(listener) }
    }

    private fun formatJoinDate(millis: Long): String =
        if (millis <= 0L) "" else SimpleDateFormat("MMMM yyyy", Locale("id", "ID")).format(Date(millis))

    // ---------- perhitungan ----------

    private fun achievement(
        id: String, title: String, description: String, progress: Int, target: Int
    ) = Achievement(
        id = id,
        title = title,
        description = description,
        unlocked = progress >= target,
        progress = progress.coerceAtMost(target),
        target = target
    )

    private fun unlockedLevel(completed: Set<String>): Int {
        var level = 1
        for (def in LevelQuizConfig.levels) {
            val allDone = LevelQuizConfig.checkpointsForLevel(def.id).all { it.id in completed }
            if (allDone) level = def.id + 1 else break
        }
        return level.coerceAtMost(LevelQuizConfig.levels.size)
    }

    private fun currentStreak(timestamps: List<Long>): Int {
        val days = timestamps.map(::dayIndex).toSet()
        val today = dayIndex(System.currentTimeMillis())
        var cursor = when {
            today in days -> today
            (today - 1) in days -> today - 1
            else -> return 0
        }
        var streak = 0
        while (cursor in days) {
            streak++
            cursor--
        }
        return streak
    }

    private fun longestStreak(timestamps: List<Long>): Int {
        val days = timestamps.map(::dayIndex).toSortedSet()
        var best = 0
        var run = 0
        var prev: Long? = null
        for (d in days) {
            run = if (prev != null && d == prev + 1) run + 1 else 1
            best = maxOf(best, run)
            prev = d
        }
        return best
    }

    private fun dayIndex(millis: Long): Long =
        (millis + TimeZone.getDefault().getOffset(millis)) / DAY_MS

    private companion object {
        const val DAY_MS = 24L * 60 * 60 * 1000
    }
}

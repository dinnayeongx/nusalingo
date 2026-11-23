package com.example.nusalingo.data.sync

import com.example.nusalingo.data.local.LessonDao
import com.example.nusalingo.data.local.UserPreferences
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class ProgressSyncManager(
    private val lessonDao: LessonDao,
    private val userPreferences: UserPreferences
) {
    suspend fun pushForLogout(userId: String): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            val db = try {
                FirebaseFirestore.getInstance()
            } catch (e: Exception) {
                null
            }
            if (db != null) {
                val xp = userPreferences.totalXp.first()
                val completed = userPreferences.completedLevelCheckpoints.first()
                val scores = userPreferences.levelCheckpointScores.first()
                val history = lessonDao.getHistory().first()

                val data = mapOf(
                    "totalXp" to xp,
                    "completedCheckpoints" to completed.toList(),
                    "checkpointScores" to scores,
                    "historyCount" to history.size,
                    "updatedAt" to FieldValue.serverTimestamp()
                )
                db.collection("users").document(userId).collection("progress").document("summary")
                    .set(data, SetOptions.merge())
                    .await()
            }
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun clearLocalProgress() = withContext(Dispatchers.IO) {
        try {
            lessonDao.clearHistory()
            userPreferences.clearProgress()
        } catch (e: Exception) {
            // ignore
        }
    }
}

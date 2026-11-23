package com.example.nusalingo.data.model

enum class ActivityType { LESSON, QUIZ, PRACTICE }

data class ActivityHistory(
    val id: String = "",
    val type: ActivityType = ActivityType.LESSON,
    val title: String = "",
    val score: Int? = null,      // null kalau aktivitas tidak punya skor (misal lesson)
    val xpEarned: Int? = null,   // null kalau aktivitas tidak memberi XP
    val timestamp: Long = 0L,    // epoch millis
    val learnedCount: Int? = null // jumlah aksara yang dipelajari (khusus lesson)
)

package com.example.nusalingo.data

import com.example.nusalingo.feature.quiz.screen.QuizType

data class LevelCheckpoint(
    val id: String,
    val levelId: Int,
    val checkpointNumber: Int,
    val title: String,
    val description: String,
    val questionCount: Int = 10
)

data class LevelDefinition(
    val id: Int,
    val title: String,
    val description: String
)

object LevelQuizConfig {
    const val QUESTIONS_PER_CHECKPOINT = 10
    const val POINTS_PER_CORRECT_ANSWER = 10
    const val XP_PER_CORRECT_ANSWER = 10
    const val PASSING_SCORE = 80

    val levels = listOf(
        LevelDefinition(1, "Mengenal Aksara", "Kenali bentuk dan bunyi aksara Jawa"),
        LevelDefinition(2, "Menggabungkan Aksara", "Gabungkan aksara menjadi suku kata dan kata"),
        LevelDefinition(3, "Membangun Kalimat", "Susun kata menjadi kalimat sederhana")
    )

    val checkpoints = listOf(
        LevelCheckpoint("L1_C1", 1, 1, "Pengenalan Dasar", ""),
        LevelCheckpoint("L1_C2", 1, 2, "Pengenalan Lanjutan", ""),
        LevelCheckpoint("L1_C3", 1, 3, "Evaluasi Pengenalan", ""),

        LevelCheckpoint("L2_C1", 2, 1, "Gabungan Dasar", ""),
        LevelCheckpoint("L2_C2", 2, 2, "Pembentukan Kata", ""),
        LevelCheckpoint("L2_C3", 2, 3, "Evaluasi Gabungan", ""),

        LevelCheckpoint("L3_C1", 3, 1, "Pengenalan Kata", ""),
        LevelCheckpoint("L3_C2", 3, 2, "Penyusunan Kalimat", ""),
        LevelCheckpoint("L3_C3", 3, 3, "Evaluasi Akhir", "")
    )

    fun availableTypes(levelId: Int): List<QuizType> {
        return when (levelId) {
            1 -> listOf(
                QuizType.MULTIPLE_CHOICE,
                QuizType.WRITE_CHARACTER,
                QuizType.MATCHING_PAIRS
            )

            2 -> listOf(
                QuizType.MULTIPLE_CHOICE,
                QuizType.WRITE_CHARACTER,
                QuizType.MATCHING_PAIRS,
                QuizType.RANGKAI_AKSARA
            )

            3 -> QuizType.values().toList()

            else -> emptyList()
        }
    }

    fun checkpointsForLevel(levelId: Int): List<LevelCheckpoint> = checkpoints.filter { it.levelId == levelId }
    fun checkpoint(checkpointId: String): LevelCheckpoint? = checkpoints.firstOrNull { it.id == checkpointId }

    fun createSession(levelId: Int): List<QuizType> {
        val types = availableTypes(levelId)

        require(types.isNotEmpty()) {
            "No quiz types available for this level."
        }

        val shuffledTypes = types.shuffled()

        val remainingCount = QUESTIONS_PER_CHECKPOINT - shuffledTypes.size

        val additionalTypes = if (remainingCount > 0) {
            List(remainingCount) { types.random() }
        } else {
            emptyList()
        }

        return (shuffledTypes + additionalTypes)
            .shuffled()
            .take(QUESTIONS_PER_CHECKPOINT)
    }

    fun isPassed(correctAnswers: Int): Boolean = correctAnswers * POINTS_PER_CORRECT_ANSWER >= PASSING_SCORE
}
package com.example.nusalingo.screens.quiz

import androidx.compose.ui.geometry.Offset

data class QuizState(
    val quizType: QuizType = QuizType.MULTIPLE_CHOICE,
    val currentQuestionIndex: Int = 0,
    val totalQuestions: Int = 5,
    val correctAnswers: Int = 0,
    val xpEarned: Int = 0,
    val isChecked: Boolean = false,
    val isFinished: Boolean = false,

    val selectedOption: String? = null,

    val placedTokenIndices: List<Int> = emptyList(),

    val selectedCharacterIndex: Int? = null,
    val selectedReadingIndex: Int? = null,
    val matchedPairIndices: Set<Int> = emptySet(),
    val isLastPairCorrect: Boolean = false,

    val strokes: List<List<Offset>> = emptyList()
) {
    val progress: Float
        get() = if (totalQuestions > 0) {
            (currentQuestionIndex + if (isChecked) 1 else 0).toFloat() / totalQuestions
        } else 0f

    val accuracyPercent: Int
        get() = if (totalQuestions > 0) (correctAnswers * 100) / totalQuestions else 0
}
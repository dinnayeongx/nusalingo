package com.example.nusalingo.data

import com.example.nusalingo.screens.quiz.QuizType

object LevelQuizConfig {

    fun availableTypes(levelId: Int): List<QuizType> {
        return when (levelId) {
            1 -> listOf(
                QuizType.MULTIPLE_CHOICE,
                QuizType.MATCHING_PAIRS,
                QuizType.WRITE_CHARACTER,
                QuizType.ALIH_AKSARA,
                QuizType.AUDIO_QUIZ,
                QuizType.RANGKAI_AKSARA
            )

            else -> listOf(
                QuizType.MULTIPLE_CHOICE,
                QuizType.MATCHING_PAIRS
            )
        }
    }

    fun createSession(
        levelId: Int,
        questionCount: Int = 10
    ): List<QuizType> {
        val available = availableTypes(levelId)

        return List(questionCount) {
            available.random()
        }
    }
}
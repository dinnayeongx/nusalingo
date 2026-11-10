package com.example.nusalingo.feature.practice.ui

import com.example.nusalingo.feature.quiz.screen.QuizType

data class PracticeItem(
    val title: String,
    val type: QuizType
)

data class PracticeState(
    val practiceItems: List<PracticeItem> = emptyList(),
    val isLoading: Boolean = false
)
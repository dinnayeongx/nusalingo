package com.example.nusalingo.screens.practice

import com.example.nusalingo.screens.quiz.QuizType

data class PracticeItem(
    val title: String,
    val type: QuizType
)

data class PracticeState(
    val practiceItems: List<PracticeItem> = emptyList(),
    val isLoading: Boolean = false
)
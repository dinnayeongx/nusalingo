package com.example.nusalingo.screens.quiz

import androidx.compose.runtime.Composable
import com.example.nusalingo.components.quiz.BubbleType

private val AudioQuizQuestions = listOf(
    QuizAksara(
        bubble = "ꦧꦕ",
        answer = listOf("BA", "CA"),
        bank = listOf("CA", "BA", "TA")
    ),
    QuizAksara(
        bubble = "ꦤꦩ",
        answer = listOf("NA", "MA"),
        bank = listOf("MA", "NA", "RA")
    ),
    QuizAksara(
        bubble = "ꦫꦩ",
        answer = listOf("RA", "MA"),
        bank = listOf("MA", "LA", "RA")
    )
)

@Composable
fun AudioQuizScreen(
    onExit: () -> Unit,
    onFinish: (
        correctAnswers: Int,
        totalQuestions: Int,
        xpEarned: Int
    ) -> Unit
) {
    QuizAksaraContent(
        questions = AudioQuizQuestions,
        bubbleType = BubbleType.AKSARA_AUDIO,
        tokensAreAksara = false,
        onExit = onExit,
        onFinish = onFinish
    )
}
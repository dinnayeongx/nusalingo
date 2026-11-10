package com.example.nusalingo.feature.quiz.screen

import androidx.compose.runtime.Composable
import com.example.nusalingo.feature.quiz.QuizAksara
import com.example.nusalingo.feature.quiz.QuizAksaraContent
import com.example.nusalingo.ui.components.quiz.BubbleType

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
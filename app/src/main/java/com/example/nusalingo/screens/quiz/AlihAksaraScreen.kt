package com.example.nusalingo.screens.quiz

import androidx.compose.runtime.Composable
import com.example.nusalingo.components.quiz.BubbleType

private val AlihAksaraQuestions = listOf(
    QuizAksara(
        bubble = "ꦲꦤꦏꦕꦩꦠ",
        answer = listOf("Ana", "Kacamata"),
        bank = listOf("Ana", "Laksamana", "Nata", "Pada", "Kacamata")
    ),
    QuizAksara(
        bubble = "ꦧꦭꦫꦩ",
        answer = listOf("Bala", "Rama"),
        bank = listOf("Rama", "Nama", "Bala", "Cara")
    )
)

@Composable
fun AlihAksaraScreen(
    onExit: () -> Unit,
    onFinish: (
        correctAnswers: Int,
        totalQuestions: Int,
        xpEarned: Int
    ) -> Unit
) {
    QuizAksaraContent(
        questions = AlihAksaraQuestions,
        bubbleType = BubbleType.AKSARA_LARGE,
        tokensAreAksara = false,
        onExit = onExit,
        onFinish = onFinish
    )
}
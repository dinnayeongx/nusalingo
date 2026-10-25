package com.example.nusalingo.screens.quiz

import androidx.compose.runtime.Composable
import com.example.nusalingo.components.quiz.BubbleType

private val RangkaiAksaraQuestions = listOf(
    QuizAksara(
        bubble = "BALA RAMA",
        answer = listOf("ꦧ", "ꦭ", "ꦫ", "ꦩ"),
        bank = listOf("ꦭ", "ꦧ", "ꦤ", "ꦩ", "ꦫ")
    ),
    QuizAksara(
        bubble = "CARA",
        answer = listOf("ꦕ", "ꦫ"),
        bank = listOf("ꦫ", "ꦕ", "ꦲ")
    ),
    QuizAksara(
        bubble = "NAMA",
        answer = listOf("ꦤ", "ꦩ"),
        bank = listOf("ꦩ", "ꦤ", "ꦮ")
    )
)

@Composable
fun RangkaiAksaraScreen(
    onExit: () -> Unit,
    onFinish: (
        correctAnswers: Int,
        totalQuestions: Int,
        xpEarned: Int
    ) -> Unit
) {
    QuizAksaraContent(
        questions = RangkaiAksaraQuestions,
        bubbleType = BubbleType.LATIN_TEXT,
        tokensAreAksara = true,
        onExit = onExit,
        onFinish = onFinish
    )
}
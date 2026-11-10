package com.example.nusalingo.feature.quiz.screen

import androidx.compose.runtime.Composable
import com.example.nusalingo.feature.quiz.QuizAksara
import com.example.nusalingo.feature.quiz.QuizAksaraContent
import com.example.nusalingo.ui.components.quiz.BubbleType

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
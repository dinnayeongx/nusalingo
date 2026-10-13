package com.example.nusalingo.screens.quiz

import androidx.compose.runtime.*

enum class QuizType {
    MULTIPLE_CHOICE,
    MATCHING_PAIRS,
    WRITE_CHARACTER,
    ALIH_AKSARA,
    AUDIO_QUIZ,
    RANGKAI_AKSARA
}

@Composable
fun QuizFlowScreen(
    quizType: QuizType = QuizType.ALIH_AKSARA
) {
    var showResult by remember { mutableStateOf(false) }
    var correctAnswers by remember { mutableIntStateOf(0) }
    var totalQuestions by remember { mutableIntStateOf(0) }
    var xpEarned by remember { mutableIntStateOf(0) }

    val finishQuiz: (Int, Int, Int) -> Unit = { correct, total, xp ->
        correctAnswers = correct
        totalQuestions = total
        xpEarned = xp
        showResult = true
    }

    if (showResult) {
        QuizResultScreen(
            xpEarned = xpEarned,
            correctAnswers = correctAnswers,
            totalQuestions = totalQuestions,
            onContinue = { showResult = false },
            onHome = { showResult = false }
        )
    } else {
        when (quizType) {
            QuizType.MULTIPLE_CHOICE -> QuizScreen(
                onExit = {},
                onFinish = finishQuiz
            )

            QuizType.MATCHING_PAIRS -> MatchingPairsScreen(
                onExit = {},
                onFinish = finishQuiz
            )

            QuizType.WRITE_CHARACTER -> WriteCharacterScreen(
                onExit = {},
                onFinish = finishQuiz
            )

            QuizType.ALIH_AKSARA-> AlihAksaraScreen(
                onExit = {},
                onFinish = finishQuiz
            )

            QuizType.AUDIO_QUIZ -> AudioQuizScreen(
                onExit = {},
                onFinish = finishQuiz
            )

            QuizType.RANGKAI_AKSARA -> RangkaiAksaraScreen(
                onExit = {},
                onFinish = finishQuiz
            )
        }
    }
}
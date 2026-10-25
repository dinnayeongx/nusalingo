package com.example.nusalingo.screens.quiz

import androidx.compose.runtime.*
import androidx.lifecycle.viewmodel.compose.viewModel

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

@Composable
fun QuizFlowRoute(
    quizType: QuizType = QuizType.ALIH_AKSARA,
    onNavigateBack: () -> Unit = {},
    viewModel: QuizViewModel = viewModel()
) {
    val state by viewModel.uiState.collectAsState()

    LaunchedEffect(quizType) {
        viewModel.startQuiz(quizType)
    }

    QuizFlowScreen(
        state = state,
        quizType = quizType,
        onFinish = { correct, total, xp ->
            viewModel.submitAnswer(true)
        },
        onHome = {
            viewModel.resetQuiz()
            onNavigateBack()
        }
    )
}

@Composable
fun QuizFlowScreen(
    state: QuizState,
    quizType: QuizType = QuizType.ALIH_AKSARA,
    onFinish: (correct: Int, total: Int, xp: Int) -> Unit,
    onHome: () -> Unit
) {
    var showResult by remember { mutableStateOf(false) }

    if (showResult || state.isFinished) {
        QuizResultScreen(
            xpEarned = state.xpEarned,
            correctAnswers = state.correctAnswers,
            totalQuestions = state.totalQuestions,
            onContinue = { showResult = false },
            onHome = onHome
        )
    } else {
        val handleFinish: (Int, Int, Int) -> Unit = { correct, total, xp ->
            onFinish(correct, total, xp)
            showResult = true
        }

        when (quizType) {
            QuizType.MULTIPLE_CHOICE -> QuizScreen(onExit = onHome, onFinish = handleFinish)
            QuizType.MATCHING_PAIRS -> MatchingPairsScreen(onExit = onHome, onFinish = handleFinish)
            QuizType.WRITE_CHARACTER -> WriteCharacterScreen(onExit = onHome, onFinish = handleFinish)
            QuizType.ALIH_AKSARA -> AlihAksaraScreen(onExit = onHome, onFinish = handleFinish)
            QuizType.AUDIO_QUIZ -> AudioQuizScreen(onExit = onHome, onFinish = handleFinish)
            QuizType.RANGKAI_AKSARA -> RangkaiAksaraScreen(onExit = onHome, onFinish = handleFinish)
        }
    }
}
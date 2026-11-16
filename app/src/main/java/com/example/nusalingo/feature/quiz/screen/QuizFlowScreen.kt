package com.example.nusalingo.feature.quiz.screen

import androidx.compose.runtime.*
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.ui.platform.LocalContext
import com.example.nusalingo.data.local.UserPreferences
import com.example.nusalingo.feature.quiz.ui.QuizState
import com.example.nusalingo.feature.quiz.ui.QuizViewModel
import kotlinx.coroutines.launch

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
    if (quizType == QuizType.MULTIPLE_CHOICE) {
        TebakAksaraQuizRoute(onNavigateBack = onNavigateBack)
    } else if (quizType == QuizType.MATCHING_PAIRS) {
        MatchingPairsQuizRoute(onNavigateBack = onNavigateBack)
    } else if (quizType == QuizType.ALIH_AKSARA) {
        AlihAksaraQuizRoute(onNavigateBack = onNavigateBack)
    } else if (quizType == QuizType.WRITE_CHARACTER) {
        WriteCharacterQuizRoute(onNavigateBack = onNavigateBack)
    } else if (quizType == QuizType.RANGKAI_AKSARA) {
        RangkaiAksaraQuizRoute(onNavigateBack = onNavigateBack)
    } else if (quizType == QuizType.AUDIO_QUIZ) {
        AudioQuizRoute(onNavigateBack = onNavigateBack)
    } else {
        val state by viewModel.uiState.collectAsState()

        LaunchedEffect(quizType) {
            viewModel.startQuiz(quizType)
        }

        QuizFlowScreen(
            state = state,
            quizType = quizType,
            onFinish = { _, _, _ ->
                viewModel.submitAnswer(true)
            },
            onHome = {
                viewModel.resetQuiz()
                onNavigateBack()
            }
        )
    }
}

private data class MatchingQuizResult(
    val correctAnswers: Int,
    val totalQuestions: Int,
    val xpEarned: Int
)

private data class AlihAksaraQuizResult(
    val correctAnswers: Int,
    val totalQuestions: Int,
    val xpEarned: Int,
    val wrongQuestionIndices: List<Int>
)

private data class WriteCharacterQuizResult(
    val correctAnswers: Int,
    val totalQuestions: Int,
    val xpEarned: Int,
    val wrongQuestionIndices: List<Int>
)

private data class RangkaiAksaraQuizResult(
    val correctAnswers: Int,
    val totalQuestions: Int,
    val xpEarned: Int,
    val wrongQuestionIndices: List<Int>
)

private data class TebakAksaraQuizResult(
    val correctAnswers: Int,
    val totalQuestions: Int,
    val xpEarned: Int,
    val wrongQuestionIndices: List<Int>
)

private data class AudioQuizResult(
    val correctAnswers: Int,
    val totalQuestions: Int,
    val xpEarned: Int,
    val wrongQuestionIndices: List<Int>
)

@Composable
private fun MatchingPairsQuizRoute(onNavigateBack: () -> Unit) {
    val context = LocalContext.current
    val userPreferences = remember(context) { UserPreferences(context) }
    var result by remember { mutableStateOf<MatchingQuizResult?>(null) }
    var totalXp by remember { mutableStateOf<Int?>(null) }

    LaunchedEffect(result) {
        val completedQuiz = result ?: return@LaunchedEffect
        totalXp = userPreferences.addXp(completedQuiz.xpEarned)
    }

    val finishedQuiz = result
    val savedTotalXp = totalXp
    if (finishedQuiz == null) {
        MatchingPairsScreen(
            onExit = onNavigateBack,
            onFinish = { correct, total, xp ->
                result = MatchingQuizResult(correct, total, xp)
            }
        )
    } else {
        QuizResultScreen(
            xpEarned = finishedQuiz.xpEarned,
            correctAnswers = finishedQuiz.correctAnswers,
            totalQuestions = finishedQuiz.totalQuestions,
            onContinue = onNavigateBack,
            onHome = onNavigateBack,
            continueText = "Kembali ke Latihan",
            showStats = false,
            totalXp = savedTotalXp
        )
    }
}

@Composable
private fun AlihAksaraQuizRoute(onNavigateBack: () -> Unit) {
    val context = LocalContext.current
    val userPreferences = remember(context) { UserPreferences(context) }
    val coroutineScope = rememberCoroutineScope()
    var result by remember { mutableStateOf<AlihAksaraQuizResult?>(null) }
    var wrongAnswers by remember { mutableStateOf(emptyList<QuizReviewAnswer>()) }
    var questionIndices by remember {
        mutableStateOf((0 until ALIH_AKSARA_QUESTION_COUNT).toList())
    }
    var wrongQuestionIndices by remember { mutableStateOf(emptyList<Int>()) }
    var isRetryAttempt by remember { mutableStateOf(false) }
    var attemptId by remember { mutableIntStateOf(0) }
    var initialXpEarned by remember { mutableIntStateOf(0) }
    var showAnswerReview by remember { mutableStateOf(false) }
    var totalXp by remember { mutableStateOf<Int?>(null) }

    LaunchedEffect(result) {
        val completedQuiz = result ?: return@LaunchedEffect
        totalXp = userPreferences.addXp(completedQuiz.xpEarned)
    }

    val finishedQuiz = result
    val savedTotalXp = totalXp
    if (finishedQuiz == null) {
        AlihAksaraScreen(
            onExit = onNavigateBack,
            onWrongAnswers = {
                wrongAnswers = it
                wrongQuestionIndices = it.map { answer -> answer.questionNumber - 1 }
            },
            questionIndices = questionIndices,
            awardXp = !isRetryAttempt,
            attemptId = attemptId,
            onFinish = { _, _, xp ->
                val xpEarned = if (isRetryAttempt) {
                    initialXpEarned
                } else {
                    initialXpEarned = xp
                    coroutineScope.launch {
                        totalXp = userPreferences.addXp(xp)
                    }
                    xp
                }
                result = AlihAksaraQuizResult(
                    correctAnswers = ALIH_AKSARA_QUESTION_COUNT - wrongQuestionIndices.size,
                    totalQuestions = ALIH_AKSARA_QUESTION_COUNT,
                    xpEarned = xpEarned,
                    wrongQuestionIndices = wrongQuestionIndices
                )
            }
        )
    } else if (showAnswerReview) {
        QuizAnswersScreen(
            wrongAnswers = wrongAnswers,
            onRetry = {
                questionIndices = finishedQuiz.wrongQuestionIndices
                wrongQuestionIndices = emptyList()
                result = null
                wrongAnswers = emptyList()
                showAnswerReview = false
                isRetryAttempt = true
                attemptId++
            },
            onHome = onNavigateBack,
            retryEnabled = finishedQuiz.wrongQuestionIndices.isNotEmpty()
        )
    } else {
        QuizResultScreen(
            xpEarned = finishedQuiz.xpEarned,
            correctAnswers = finishedQuiz.correctAnswers,
            totalQuestions = finishedQuiz.totalQuestions,
            onContinue = { showAnswerReview = true },
            onHome = onNavigateBack,
            continueText = "Lihat Jawaban",
            totalXp = savedTotalXp,
            secondaryActionText = "Kembali ke Latihan"
        )
    }
}

@Composable
private fun RangkaiAksaraQuizRoute(onNavigateBack: () -> Unit) {
    val context = LocalContext.current
    val userPreferences = remember(context) { UserPreferences(context) }
    val coroutineScope = rememberCoroutineScope()
    var result by remember { mutableStateOf<RangkaiAksaraQuizResult?>(null) }
    var wrongAnswers by remember { mutableStateOf(emptyList<QuizReviewAnswer>()) }
    var questionIndices by remember {
        mutableStateOf((0 until RANGKAI_AKSARA_QUESTION_COUNT).toList())
    }
    var wrongQuestionIndices by remember { mutableStateOf(emptyList<Int>()) }
    var isRetryAttempt by remember { mutableStateOf(false) }
    var attemptId by remember { mutableIntStateOf(0) }
    var initialXpEarned by remember { mutableIntStateOf(0) }
    var showAnswerReview by remember { mutableStateOf(false) }
    var totalXp by remember { mutableStateOf<Int?>(null) }

    LaunchedEffect(result) {
        val completedQuiz = result ?: return@LaunchedEffect
        totalXp = userPreferences.addXp(completedQuiz.xpEarned)
    }

    val finishedQuiz = result
    val savedTotalXp = totalXp
    if (finishedQuiz == null) {
        RangkaiAksaraScreen(
            onExit = onNavigateBack,
            onWrongAnswers = {
                wrongAnswers = it
                wrongQuestionIndices = it.map { answer -> answer.questionNumber - 1 }
            },
            questionIndices = questionIndices,
            awardXp = !isRetryAttempt,
            attemptId = attemptId,
            onFinish = { correct, total, xp ->
                val xpEarned = if (isRetryAttempt) {
                    initialXpEarned
                } else {
                    initialXpEarned = xp
                    coroutineScope.launch {
                        totalXp = userPreferences.addXp(xp)
                    }
                    xp
                }
                result = RangkaiAksaraQuizResult(
                    correctAnswers = RANGKAI_AKSARA_QUESTION_COUNT - wrongQuestionIndices.size,
                    totalQuestions = RANGKAI_AKSARA_QUESTION_COUNT,
                    xpEarned = xpEarned,
                    wrongQuestionIndices = wrongQuestionIndices
                )
            }
        )
    } else if (showAnswerReview) {
        QuizAnswersScreen(
            wrongAnswers = wrongAnswers,
            onRetry = {
                questionIndices = finishedQuiz.wrongQuestionIndices
                wrongQuestionIndices = emptyList()
                result = null
                wrongAnswers = emptyList()
                showAnswerReview = false
                isRetryAttempt = true
                attemptId++
            },
            onHome = onNavigateBack,
            homeButtonText = "Lanjut ke Latihan",
            retryEnabled = finishedQuiz.wrongQuestionIndices.isNotEmpty()
        )
    } else {
        QuizResultScreen(
            xpEarned = finishedQuiz.xpEarned,
            correctAnswers = finishedQuiz.correctAnswers,
            totalQuestions = finishedQuiz.totalQuestions,
            onContinue = { showAnswerReview = true },
            onHome = onNavigateBack,
            continueText = "Lihat Jawaban",
            totalXp = savedTotalXp,
            secondaryActionText = "Lanjut ke Latihan"
        )
    }
}

@Composable
private fun TebakAksaraQuizRoute(onNavigateBack: () -> Unit) {
    val context = LocalContext.current
    val userPreferences = remember(context) { UserPreferences(context) }
    val coroutineScope = rememberCoroutineScope()
    var result by remember { mutableStateOf<TebakAksaraQuizResult?>(null) }
    var questionIndices by remember {
        mutableStateOf((0 until TEBAK_AKSARA_QUESTION_COUNT).toList())
    }
    var wrongQuestionIndices by remember { mutableStateOf(emptyList<Int>()) }
    var isRetryAttempt by remember { mutableStateOf(false) }
    var attemptId by remember { mutableIntStateOf(0) }
    var initialXpEarned by remember { mutableIntStateOf(0) }
    var totalXp by remember { mutableStateOf<Int?>(null) }

    val finishedQuiz = result
    val savedTotalXp = totalXp
    if (finishedQuiz == null) {
        QuizScreen(
            onExit = onNavigateBack,
            questionIndices = questionIndices,
            awardXp = !isRetryAttempt,
            attemptId = attemptId,
            onIncorrectQuestions = { wrongQuestionIndices = it },
            onFinish = { _, _, xp ->
                val xpEarned = if (isRetryAttempt) {
                    initialXpEarned
                } else {
                    initialXpEarned = xp
                    coroutineScope.launch {
                        totalXp = userPreferences.addXp(xp)
                    }
                    xp
                }
                result = TebakAksaraQuizResult(
                    correctAnswers = TEBAK_AKSARA_QUESTION_COUNT - wrongQuestionIndices.size,
                    totalQuestions = TEBAK_AKSARA_QUESTION_COUNT,
                    xpEarned = xpEarned,
                    wrongQuestionIndices = wrongQuestionIndices
                )
            }
        )
    } else {
        QuizResultScreen(
            xpEarned = finishedQuiz.xpEarned,
            correctAnswers = finishedQuiz.correctAnswers,
            totalQuestions = finishedQuiz.totalQuestions,
            onContinue = {
                questionIndices = finishedQuiz.wrongQuestionIndices
                wrongQuestionIndices = emptyList()
                isRetryAttempt = true
                attemptId++
                result = null
            },
            onHome = onNavigateBack,
            continueText = "Kerjakan Ulang",
            totalXp = savedTotalXp,
            secondaryActionText = "Kembali ke Latihan",
            continueEnabled = finishedQuiz.wrongQuestionIndices.isNotEmpty()
        )
    }
}

@Composable
private fun AudioQuizRoute(onNavigateBack: () -> Unit) {
    val context = LocalContext.current
    val userPreferences = remember(context) { UserPreferences(context) }
    val coroutineScope = rememberCoroutineScope()
    var result by remember { mutableStateOf<AudioQuizResult?>(null) }
    var questionIndices by remember {
        mutableStateOf((0 until AUDIO_QUIZ_QUESTION_COUNT).toList())
    }
    var wrongQuestionIndices by remember { mutableStateOf(emptyList<Int>()) }
    var isRetryAttempt by remember { mutableStateOf(false) }
    var attemptId by remember { mutableIntStateOf(0) }
    var initialXpEarned by remember { mutableIntStateOf(0) }
    var totalXp by remember { mutableStateOf<Int?>(null) }

    val finishedQuiz = result
    val savedTotalXp = totalXp
    if (finishedQuiz == null) {
        AudioQuizScreen(
            onExit = onNavigateBack,
            questionIndices = questionIndices,
            awardXp = !isRetryAttempt,
            attemptId = attemptId,
            onIncorrectQuestions = { wrongQuestionIndices = it },
            onFinish = { _, _, xp ->
                val xpEarned = if (isRetryAttempt) {
                    initialXpEarned
                } else {
                    initialXpEarned = xp
                    coroutineScope.launch {
                        totalXp = userPreferences.addXp(xp)
                    }
                    xp
                }
                result = AudioQuizResult(
                    correctAnswers = AUDIO_QUIZ_QUESTION_COUNT - wrongQuestionIndices.size,
                    totalQuestions = AUDIO_QUIZ_QUESTION_COUNT,
                    xpEarned = xpEarned,
                    wrongQuestionIndices = wrongQuestionIndices
                )
            }
        )
    } else {
        QuizResultScreen(
            xpEarned = finishedQuiz.xpEarned,
            correctAnswers = finishedQuiz.correctAnswers,
            totalQuestions = finishedQuiz.totalQuestions,
            onContinue = {
                questionIndices = finishedQuiz.wrongQuestionIndices
                wrongQuestionIndices = emptyList()
                isRetryAttempt = true
                attemptId++
                result = null
            },
            onHome = onNavigateBack,
            continueText = "Kerjakan Ulang",
            totalXp = savedTotalXp,
            secondaryActionText = "Kembali ke Latihan",
            continueEnabled = finishedQuiz.wrongQuestionIndices.isNotEmpty()
        )
    }
}

@Composable
private fun WriteCharacterQuizRoute(onNavigateBack: () -> Unit) {
    val context = LocalContext.current
    val userPreferences = remember(context) { UserPreferences(context) }
    val coroutineScope = rememberCoroutineScope()
    var result by remember { mutableStateOf<WriteCharacterQuizResult?>(null) }
    var questionIndices by remember {
        mutableStateOf((0 until WRITE_CHARACTER_QUESTION_COUNT).toList())
    }
    var wrongQuestionIndices by remember { mutableStateOf(emptyList<Int>()) }
    var isRetryAttempt by remember { mutableStateOf(false) }
    var attemptId by remember { mutableIntStateOf(0) }
    var initialXpEarned by remember { mutableIntStateOf(0) }
    var totalXp by remember { mutableStateOf<Int?>(null) }

    val finishedQuiz = result
    val savedTotalXp = totalXp
    if (finishedQuiz == null) {
        WriteCharacterScreen(
            onExit = onNavigateBack,
            questionIndices = questionIndices,
            awardXp = !isRetryAttempt,
            attemptId = attemptId,
            onIncorrectQuestions = { wrongQuestionIndices = it },
            onFinish = { _, _, xp ->
                val xpEarned = if (isRetryAttempt) {
                    initialXpEarned
                } else {
                    initialXpEarned = xp
                    coroutineScope.launch {
                        totalXp = userPreferences.addXp(xp)
                    }
                    xp
                }
                result = WriteCharacterQuizResult(
                    correctAnswers = WRITE_CHARACTER_QUESTION_COUNT - wrongQuestionIndices.size,
                    totalQuestions = WRITE_CHARACTER_QUESTION_COUNT,
                    xpEarned = xpEarned,
                    wrongQuestionIndices = wrongQuestionIndices
                )
            }
        )
    } else {
        QuizResultScreen(
            xpEarned = finishedQuiz.xpEarned,
            correctAnswers = finishedQuiz.correctAnswers,
            totalQuestions = finishedQuiz.totalQuestions,
            onContinue = {
                questionIndices = finishedQuiz.wrongQuestionIndices
                isRetryAttempt = true
                attemptId++
                result = null
            },
            onHome = onNavigateBack,
            continueText = "Kerjakan Ulang",
            totalXp = savedTotalXp,
            secondaryActionText = "Kembali ke Latihan",
            continueEnabled = finishedQuiz.wrongQuestionIndices.isNotEmpty()
        )
    }
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
            QuizType.WRITE_CHARACTER -> WriteCharacterScreen(
                onExit = onHome,
                onFinish = handleFinish
            )
            QuizType.ALIH_AKSARA -> AlihAksaraScreen(onExit = onHome, onFinish = handleFinish)
            QuizType.AUDIO_QUIZ -> AudioQuizScreen(onExit = onHome, onFinish = handleFinish)
            QuizType.RANGKAI_AKSARA -> RangkaiAksaraScreen(onExit = onHome, onFinish = handleFinish)
        }
    }
}
package com.example.nusalingo.feature.level.screen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import com.example.nusalingo.data.LevelCheckpoint
import com.example.nusalingo.data.LevelQuizConfig
import com.example.nusalingo.data.local.UserPreferences
import com.example.nusalingo.feature.quiz.screen.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.first

private data class LevelQuestionStep(
    val type: QuizType,
    val questionIndex: Int
)

private fun questionCount(type: QuizType): Int = when (type) {
    QuizType.MULTIPLE_CHOICE -> TEBAK_AKSARA_QUESTION_COUNT
    QuizType.MATCHING_PAIRS -> 1
    QuizType.WRITE_CHARACTER -> WRITE_CHARACTER_QUESTION_COUNT
    QuizType.ALIH_AKSARA -> ALIH_AKSARA_QUESTION_COUNT
    QuizType.AUDIO_QUIZ -> AUDIO_QUIZ_QUESTION_COUNT
    QuizType.RANGKAI_AKSARA -> RANGKAI_AKSARA_QUESTION_COUNT
}

private fun createLevelQuestions(levelId: Int): List<LevelQuestionStep> {
    val types = LevelQuizConfig.createSession(levelId)

    val usedIndices = mutableMapOf<QuizType, MutableSet<Int>>()

    return types.map { type ->
        val count = questionCount(type)
        val used = usedIndices.getOrPut(type) { mutableSetOf() }

        val available = (0 until count).filterNot { it in used }

        val index = if (available.isNotEmpty()) {
            available.random()
        } else {
            (0 until count).random()
        }

        used.add(index)

        LevelQuestionStep(type, index)
    }
}

@Composable
fun LevelQuizRoute(
    levelId: Int,
    onBack: () -> Unit
) {
    val context = LocalContext.current
    val preferences = remember(context) {
        UserPreferences(context)
    }

    val completed by preferences.completedLevelCheckpoints.collectAsState(
        initial = emptySet()
    )

    val checkpoints = remember(levelId) {
        LevelQuizConfig.checkpointsForLevel(levelId)
    }

    var currentCheckpointIndex by remember(levelId) {
        mutableIntStateOf(
            checkpoints.indexOfFirst { it.id !in completed }
                .let { if (it == -1) checkpoints.lastIndex else it }
        )
    }

    if (checkpoints.isEmpty()) {
        onBack()
        return
    }

    val currentCheckpoint = checkpoints[currentCheckpointIndex]

    key(currentCheckpoint.id) {
        LevelQuizScreen(
            checkpoint = currentCheckpoint,
            preferences = preferences,
            onBack = onBack,
            onContinue = {
                if (currentCheckpointIndex < checkpoints.lastIndex) {
                    currentCheckpointIndex++
                } else {
                    onBack()
                }
            }
        )
    }
}

@Composable
private fun LevelQuizScreen(
    checkpoint: LevelCheckpoint,
    preferences: UserPreferences,
    onBack: () -> Unit,
    onContinue: () -> Unit
) {
    val scope = rememberCoroutineScope()

    val totalXp by preferences.totalXp.collectAsState(initial = 0)

    val questions = remember(checkpoint.id) {
        createLevelQuestions(checkpoint.levelId)
    }

    var questionIndex by remember(checkpoint.id) {
        mutableIntStateOf(0)
    }

    var correctAnswers by remember(checkpoint.id) {
        mutableIntStateOf(0)
    }

    var isFinished by remember(checkpoint.id) {
        mutableStateOf(false)
    }

    var isPassed by remember(checkpoint.id) {
        mutableStateOf(false)
    }

    var xpEarned by remember(checkpoint.id) {
        mutableIntStateOf(0)
    }

    var savedTotalXp by remember(checkpoint.id) {
        mutableIntStateOf(totalXp)
    }

    var isSaving by remember(checkpoint.id) {
        mutableStateOf(false)
    }

    fun finishQuestion(isCorrect: Boolean) {
        if (isFinished || isSaving) return

        val updatedCorrect =
            correctAnswers + if (isCorrect) 1 else 0

        correctAnswers = updatedCorrect

        if (questionIndex < questions.lastIndex) {
            questionIndex++
        } else {
            val score =
                updatedCorrect * LevelQuizConfig.POINTS_PER_CORRECT_ANSWER

            val passed = LevelQuizConfig.isPassed(updatedCorrect)

            isSaving = true
            isPassed = passed

            scope.launch {
                val newlyCompleted =
                    preferences.completeLevelCheckpoint(checkpoint.id, score)

                savedTotalXp = preferences.totalXp.first()

                xpEarned = if (passed && newlyCompleted) {
                    score
                } else {
                    0
                }

                isFinished = true
                isSaving = false
            }
        }
    }

    if (isFinished) {
        QuizResultScreen(
            xpEarned = xpEarned,
            correctAnswers = correctAnswers,
            totalQuestions = LevelQuizConfig.QUESTIONS_PER_CHECKPOINT,

            onContinue = {
                if (isPassed) {
                    onContinue()
                } else {
                    questionIndex = 0
                    correctAnswers = 0
                    isFinished = false
                    isPassed = false
                    xpEarned = 0
                    isSaving = false
                }
            },

            onHome = onBack,

            continueText = if (isPassed) {
                "Lanjutkan"
            } else {
                "Coba Lagi"
            },

            totalXp = savedTotalXp,

            resultTitle = if (isPassed) {
                "Checkpoint Berhasil!"
            } else {
                "Belum Lulus"
            },

            resultDescription = if (isPassed) {
                "Nilai kamu ${correctAnswers * 10}/100. Tahap berikutnya sudah terbuka."
            } else {
                "Nilai kamu ${correctAnswers * 10}/100. Kamu membutuhkan minimal 80 untuk melanjutkan."
            }
        )
        return
    }

    val step = questions[questionIndex]

    val sessionProgress =
        questionIndex.toFloat() /
                LevelQuizConfig.QUESTIONS_PER_CHECKPOINT

    val sessionXp =
        correctAnswers * LevelQuizConfig.XP_PER_CORRECT_ANSWER

    key(checkpoint.id, questionIndex, step.type, step.questionIndex) {
        when (step.type) {
            QuizType.MULTIPLE_CHOICE -> QuizScreen(
                onExit = onBack,
                questionIndices = listOf(step.questionIndex),
                awardXp = false,
                attemptId = questionIndex,
                progressOverride = sessionProgress,
                xpOverride = sessionXp,
                onFinish = { correct, _, _ ->
                    finishQuestion(correct > 0)
                }
            )

            QuizType.MATCHING_PAIRS -> MatchingPairsScreen(
                onExit = onBack,
                levelMode = true,
                progressOverride = sessionProgress,
                xpOverride = sessionXp,
                onLevelQuestionFinished = ::finishQuestion,
                onFinish = { _, _, _ -> }
            )

            QuizType.WRITE_CHARACTER -> WriteCharacterScreen(
                onExit = onBack,
                questionIndices = listOf(step.questionIndex),
                awardXp = false,
                attemptId = questionIndex,
                progressOverride = sessionProgress,
                xpOverride = sessionXp,
                onFinish = { correct, _, _ ->
                    finishQuestion(correct > 0)
                }
            )

            QuizType.ALIH_AKSARA -> AlihAksaraScreen(
                onExit = onBack,
                questionIndices = listOf(step.questionIndex),
                awardXp = false,
                attemptId = questionIndex,
                progressOverride = sessionProgress,
                xpOverride = sessionXp,
                onFinish = { correct, _, _ ->
                    finishQuestion(correct > 0)
                }
            )

            QuizType.AUDIO_QUIZ -> AudioQuizScreen(
                onExit = onBack,
                questionIndices = listOf(step.questionIndex),
                awardXp = false,
                attemptId = questionIndex,
                progressOverride = sessionProgress,
                xpOverride = sessionXp,
                onFinish = { correct, _, _ ->
                    finishQuestion(correct > 0)
                }
            )

            QuizType.RANGKAI_AKSARA -> RangkaiAksaraScreen(
                onExit = onBack,
                questionIndices = listOf(step.questionIndex),
                awardXp = false,
                attemptId = questionIndex,
                progressOverride = sessionProgress,
                xpOverride = sessionXp,
                onFinish = { correct, _, _ ->
                    finishQuestion(correct > 0)
                }
            )
        }
    }
}

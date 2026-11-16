package com.example.nusalingo.feature.quiz

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.nusalingo.ui.components.PrimaryButton
import com.example.nusalingo.ui.components.quiz.AnswerCard
import com.example.nusalingo.ui.components.quiz.AnswerSlotRow
import com.example.nusalingo.ui.components.quiz.BubbleQuestion
import com.example.nusalingo.ui.components.quiz.BubbleType
import com.example.nusalingo.ui.components.quiz.QuizAnswerState
import com.example.nusalingo.ui.components.quiz.QuizAnswerConfetti
import com.example.nusalingo.ui.components.quiz.QuizTopBar
import com.example.nusalingo.ui.components.quiz.QuizScreenTitle
import com.example.nusalingo.ui.theme.NunitoFont
import com.example.nusalingo.ui.theme.NusalingoColors

internal data class QuizAksara(
    val bubble: String,
    val answer: List<String>,
    val bank: List<String>
) {
    init {
        val bankCounts = bank.groupingBy { it }.eachCount()
        val answerCounts = answer.groupingBy { it }.eachCount()
        require(answerCounts.all { (tile, count) -> (bankCounts[tile] ?: 0) >= count }) {
            "The answer bank must contain every required tile."
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
internal fun QuizAksaraContent(
    questions: List<QuizAksara>,
    bubbleType: BubbleType,
    tokensAreAksara: Boolean,
    onExit: () -> Unit,
    onFinish: (
        correctAnswers: Int,
        totalQuestions: Int,
        xpEarned: Int
    ) -> Unit,
    title: String = "Susun Balok Aksara menjadi Kata",
    xpPerCorrectAnswer: Int = 10,
    celebrateCorrectAnswers: Boolean = false,
    onIncorrectQuestionIndices: ((List<Int>) -> Unit)? = null,
    questionIndices: List<Int> = questions.indices.toList(),
    awardXp: Boolean = true,
    attemptId: Int = 0
) {
    val scrollState = rememberScrollState()
    require(questionIndices.isNotEmpty()) { "At least one quiz question is required." }
    require(questionIndices.all { it in questions.indices }) {
        "Quiz question indices must refer to an existing question."
    }

    var questionIndex by remember(questionIndices, attemptId) { mutableIntStateOf(0) }
    var checked by remember(questionIndices, attemptId) { mutableStateOf(false) }
    var correctAnswers by remember(questionIndices, attemptId) { mutableIntStateOf(0) }
    var confettiKey by remember(questionIndices, attemptId) { mutableIntStateOf(0) }
    var incorrectQuestionIndices by remember(questionIndices, attemptId) {
        mutableStateOf(setOf<Int>())
    }
    val placed = remember(questionIndices, attemptId) { mutableStateListOf<Int>() }

    val currentQuestionIndex = questionIndices[questionIndex]
    val question = questions[currentQuestionIndex]
    val placedTokens = placed.map { question.bank[it] }
    val isCorrect = placedTokens == question.answer
    val xp = if (awardXp) correctAnswers * xpPerCorrectAnswer else 0

    val progress = (questionIndex + if (checked) 1 else 0).toFloat() / questionIndices.size

    val slotState = when {
        checked && isCorrect -> QuizAnswerState.CORRECT
        checked && !isCorrect -> QuizAnswerState.WRONG
        else -> QuizAnswerState.DEFAULT
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(NusalingoColors.Background)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp)
                .padding(bottom = 132.dp)
                .verticalScroll(scrollState)
        ) {
            QuizTopBar(
                progress = progress,
                xp = xp,
                onExit = onExit
            )

            Spacer(Modifier.height(20.dp))

            QuizScreenTitle(text = title, fontSize = 22.sp)

            Spacer(
                Modifier.height(
                    if (bubbleType == BubbleType.LATIN_TEXT) 24.dp else 32.dp
                )
            )

            BubbleQuestion(
                text = question.bubble,
                type = bubbleType
            )

            Spacer(
                Modifier.height(
                    if (bubbleType == BubbleType.LATIN_TEXT) 28.dp else 40.dp
                )
            )

            AnswerSlotRow(
                tokens = placedTokens,
                totalSlots = question.answer.size,
                isAksara = tokensAreAksara,
                state = slotState,
                enabled = !checked,
                onTokenClick = { slotIndex ->
                    if (!checked) placed.removeAt(slotIndex)
                }
            )

            Spacer(
                Modifier.height(
                    if (bubbleType == BubbleType.LATIN_TEXT) 32.dp else 40.dp
                )
            )

            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally),
                verticalArrangement = Arrangement.spacedBy(
                    if (bubbleType == BubbleType.LATIN_TEXT) 14.dp else 10.dp
                )
            ) {
                question.bank.forEachIndexed { bankIndex, token ->
                    AnswerCard(
                        text = token,
                        isAksara = tokensAreAksara,
                        used = bankIndex in placed,
                        enabled = !checked,
                        onClick = {
                            if (placed.size < question.answer.size) {
                                placed.add(bankIndex)
                            }
                        }
                    )
                }
            }
        }

        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .background(NusalingoColors.Background)
                .navigationBarsPadding()
        ) {
            if (!checked && scrollState.maxValue > 0) {
                Text(
                    text = "Geser untuk melihat semua pilihan",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 4.dp),
                    fontSize = 12.sp,
                    color = NusalingoColors.TextSecondary
                )
            }

            if (checked) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            if (isCorrect) {
                                NusalingoColors.GreenSecondary
                            } else {
                                Color(0xFFF4B6B6)
                            }
                        )
                        .padding(
                            start = 20.dp,
                            end = 20.dp,
                            top = 16.dp,
                            bottom = 12.dp
                        )
                ) {
                    Text(
                        text = if (isCorrect) "Kamu Berhasil!" else "Belum tepat!",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = NusalingoColors.TextPrimary
                    )

                    Spacer(Modifier.height(12.dp))

                    PrimaryButton(
                        text = if (questionIndex < questionIndices.lastIndex) {
                            "Selanjutnya"
                        } else {
                            "Lihat Hasil"
                        },
                        containerColor = if (isCorrect) {
                            Color(0xFF16AD61)
                        } else {
                            Color(0xFFD64545)
                        },
                        onClick = {
                            if (questionIndex < questionIndices.lastIndex) {
                                questionIndex++
                                placed.clear()
                                checked = false
                            } else {
                                onIncorrectQuestionIndices?.invoke(
                                    incorrectQuestionIndices.sorted()
                                )
                                onFinish(
                                    correctAnswers,
                                    questionIndices.size,
                                    xp
                                )
                            }
                        }
                    )
                }
            } else {
                PrimaryButton(
                    text = "CEK",
                    onClick = {
                        if (placed.size == question.answer.size) {
                            checked = true
                            if (isCorrect) {
                                correctAnswers++
                                incorrectQuestionIndices =
                                    incorrectQuestionIndices - currentQuestionIndex
                                if (celebrateCorrectAnswers) confettiKey++
                            } else {
                                incorrectQuestionIndices =
                                    incorrectQuestionIndices + currentQuestionIndex
                            }
                        }
                    },
                    enabled = placed.size == question.answer.size,
                    modifier = Modifier.padding(
                        horizontal = 20.dp,
                        vertical = 12.dp
                    )
                )
            }
        }

        if (celebrateCorrectAnswers && checked && isCorrect) {
            QuizAnswerConfetti(key = confettiKey)
        }
    }
}
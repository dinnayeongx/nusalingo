package com.example.nusalingo.screens.quiz

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
import com.example.nusalingo.components.PrimaryButton
import com.example.nusalingo.components.quiz.AnswerCard
import com.example.nusalingo.components.quiz.AnswerSlotRow
import com.example.nusalingo.components.quiz.BubbleQuestion
import com.example.nusalingo.components.quiz.BubbleType
import com.example.nusalingo.components.quiz.QuizAnswerState
import com.example.nusalingo.components.quiz.QuizTopBar
import com.example.nusalingo.ui.theme.NusalingoColors

internal data class QuizAksara(
    val bubble: String,
    val answer: List<String>,
    val bank: List<String>
)

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
    title: String = "Susun Balok Aksara menjadi Kata"
) {
    var questionIndex by remember { mutableIntStateOf(0) }
    var checked by remember { mutableStateOf(false) }
    var correctAnswers by remember { mutableIntStateOf(0) }
    val placed = remember { mutableStateListOf<Int>() }

    val question = questions[questionIndex]
    val placedTokens = placed.map { question.bank[it] }
    val isCorrect = placedTokens == question.answer
    val xp = correctAnswers * 10

    val progress = (
            questionIndex + if (checked) 1 else 0
            ).toFloat() / questions.size

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
                .padding(bottom = 110.dp)
        ) {
            QuizTopBar(
                progress = progress,
                xp = xp,
                onExit = onExit
            )

            Spacer(Modifier.height(20.dp))

            Text(
                text = title,
                fontSize = 22.sp,
                fontWeight = FontWeight.ExtraBold,
                color = NusalingoColors.TextPrimary
            )

            Spacer(Modifier.height(32.dp))

            BubbleQuestion(
                text = question.bubble,
                type = bubbleType,
                onAudioClick = {
                    // Audio dapat ditambahkan nanti
                }
            )

            Spacer(Modifier.height(40.dp))

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

            Spacer(Modifier.height(40.dp))

            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally),
                verticalArrangement = Arrangement.spacedBy(10.dp)
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
                        text = if (questionIndex < questions.lastIndex) {
                            "Selanjutnya"
                        } else {
                            "Lihat Hasil"
                        },
                        containerColor = if (isCorrect) {
                            Color(0xFF16AD61)
                        } else {
                            NusalingoColors.ActionPrimary
                        },
                        onClick = {
                            if (questionIndex < questions.lastIndex) {
                                questionIndex++
                                placed.clear()
                                checked = false
                            } else {
                                onFinish(
                                    correctAnswers,
                                    questions.size,
                                    correctAnswers * 10
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
                            if (isCorrect) correctAnswers++
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
    }
}
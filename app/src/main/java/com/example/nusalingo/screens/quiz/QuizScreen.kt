package com.example.nusalingo.screens.quiz

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.nusalingo.components.PrimaryButton
import com.example.nusalingo.components.quiz.AnswerOption
import com.example.nusalingo.components.quiz.QuizAnswerState
import com.example.nusalingo.components.quiz.QuizFeedback
import com.example.nusalingo.components.quiz.QuizTopBar
import com.example.nusalingo.ui.theme.NusalingoColors

private data class CharacterQuestion(
    val character: String,
    val options: List<String>,
    val answer: String
)

private val sampleQuestions = listOf(
    CharacterQuestion("ꦲ", listOf("Wa", "Ha", "Ca"), "Ha"),
    CharacterQuestion("ꦤ", listOf("Na", "Ra", "Da"), "Na"),
    CharacterQuestion("ꦕ", listOf("Ca", "Sa", "Ta"), "Ca"),
    CharacterQuestion("ꦫ", listOf("Ra", "La", "Pa"), "Ra"),
    CharacterQuestion("ꦮ", listOf("Wa", "Ha", "Ma"), "Wa")
)

@Composable
fun QuizScreen(
    onExit: () -> Unit,
    onFinish: (
        correctAnswers: Int,
        totalQuestions: Int,
        xpEarned: Int
    ) -> Unit
) {
    var questionIndex by remember { mutableIntStateOf(0) }
    var selectedAnswer by remember { mutableStateOf<String?>(null) }
    var checked by remember { mutableStateOf(false) }
    var correctAnswers by remember { mutableIntStateOf(0) }

    val question = sampleQuestions[questionIndex]
    val isCorrect = selectedAnswer == question.answer
    val xp = correctAnswers * 10

    val progress = (
            questionIndex + if (checked) 1 else 0
            ).toFloat() / sampleQuestions.size

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
                text = "Kenali Hurufnya",
                fontSize = 24.sp,
                fontWeight = FontWeight.ExtraBold,
                color = NusalingoColors.TextPrimary
            )

            Spacer(Modifier.height(80.dp))

            Box(
                modifier = Modifier
                    .width(250.dp)
                    .height(250.dp)
                    .background(
                        Color(0xFFEBCB8F),
                        RoundedCornerShape(16.dp)
                    )
                    .align(Alignment.CenterHorizontally),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = question.character,
                    fontSize = 92.sp,
                    color = Color.Black,
                    textAlign = TextAlign.Center
                )
            }

            Spacer(Modifier.height(40.dp))

            Row(
                Modifier
                    .border(width = 1.dp, color = Color(0xFF8B5A2B))
                    .padding(1.dp)
                    .fillMaxWidth()
                    .height(0.dp)
            ) {}

            Spacer(Modifier.height(40.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                question.options.forEach { option ->
                    val answerState = when {
                        checked && option == question.answer ->
                            QuizAnswerState.CORRECT

                        checked && option == selectedAnswer && !isCorrect ->
                            QuizAnswerState.WRONG

                        option == selectedAnswer ->
                            QuizAnswerState.SELECTED

                        else -> QuizAnswerState.DEFAULT
                    }

                    AnswerOption(
                        text = option,
                        state = answerState,
                        onClick = {
                            if (!checked) {
                                selectedAnswer = option
                            }
                        },
                        modifier = Modifier.weight(1f)
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
                        text = if (questionIndex < sampleQuestions.lastIndex) {
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
                            if (questionIndex < sampleQuestions.lastIndex) {
                                questionIndex++
                                selectedAnswer = null
                                checked = false
                            } else {
                                onFinish(
                                    correctAnswers,
                                    sampleQuestions.size,
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
                        if (selectedAnswer != null) {
                            checked = true
                            if (isCorrect) correctAnswers++
                        }
                    },
                    enabled = selectedAnswer != null,
                    modifier = Modifier.padding(
                        horizontal = 20.dp,
                        vertical = 12.dp
                    )
                )
            }
        }
    }
}
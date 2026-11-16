package com.example.nusalingo.feature.quiz.screen

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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
import com.example.nusalingo.ui.components.PrimaryButton
import com.example.nusalingo.ui.components.quiz.AnswerOption
import com.example.nusalingo.ui.components.quiz.QuizAnswerConfetti
import com.example.nusalingo.ui.components.quiz.QuizAnswerState
import com.example.nusalingo.ui.components.quiz.QuizScreenTitle
import com.example.nusalingo.ui.components.quiz.QuizTopBar
import com.example.nusalingo.ui.theme.JavaneseFont
import com.example.nusalingo.ui.theme.NunitoFont
import com.example.nusalingo.ui.theme.NusalingoColors

internal const val TEBAK_AKSARA_QUESTION_COUNT = 10
private const val XP_PER_CORRECT_ANSWER = 5

private data class CharacterQuestion(
    val character: String,
    val options: List<String>,
    val answer: String
)

private val characterQuestions = listOf(
    CharacterQuestion("ꦏ", listOf("GA", "KA", "TA"), "KA"),
    CharacterQuestion("ꦩꦶ", listOf("MI", "MA", "NI"), "MI"),
    CharacterQuestion("ꦱꦸ", listOf("SU", "SA", "CU"), "SU"),
    CharacterQuestion("ꦧꦼ", listOf("BÊ", "BÉ", "BA"), "BÊ"),
    CharacterQuestion("ꦫꦺ", listOf("RÉ", "RÊ", "RO"), "RÉ"),
    CharacterQuestion("ꦠꦺꦴ", listOf("TO", "TU", "TÉ"), "TO"),
    CharacterQuestion("ꦚꦶ", listOf("NYI", "NYA", "NGA"), "NYI"),
    CharacterQuestion("ꦝꦸ", listOf("DHU", "DU", "DHA"), "DHU"),
    CharacterQuestion("ꦔꦼ", listOf("NGÊ", "NGÉ", "NGA"), "NGÊ"),
    CharacterQuestion("ꦛꦺꦴ", listOf("THO", "TO", "THU"), "THO")
)

@Composable
fun QuizScreen(
    onExit: () -> Unit,
    onFinish: (
        correctAnswers: Int,
        totalQuestions: Int,
        xpEarned: Int
    ) -> Unit,
    questionIndices: List<Int> = characterQuestions.indices.toList(),
    awardXp: Boolean = true,
    attemptId: Int = 0,
    onIncorrectQuestions: (List<Int>) -> Unit = {}
) {
    require(questionIndices.isNotEmpty()) { "At least one quiz question is required." }

    var questionIndex by remember(questionIndices, attemptId) { mutableIntStateOf(0) }
    var selectedAnswer by remember(questionIndices, attemptId) { mutableStateOf<String?>(null) }
    var checked by remember(questionIndices, attemptId) { mutableStateOf(false) }
    var correctAnswers by remember(questionIndices, attemptId) { mutableIntStateOf(0) }
    var confettiKey by remember(questionIndices, attemptId) { mutableIntStateOf(0) }
    var wrongQuestionIndices by remember(questionIndices, attemptId) {
        mutableStateOf(setOf<Int>())
    }

    val currentQuestionIndex = questionIndices[questionIndex]
    val question = characterQuestions[currentQuestionIndex]
    val isCorrect = selectedAnswer == question.answer
    val xp = if (awardXp) correctAnswers * XP_PER_CORRECT_ANSWER else 0
    val progress = (
        questionIndex + if (checked) 1 else 0
        ).toFloat() / questionIndices.size

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(NusalingoColors.Background)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    bottom = when {
                        checked && !isCorrect -> 184.dp
                        checked -> 160.dp
                        else -> 108.dp
                    }
                )
                .verticalScroll(rememberScrollState())
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
            ) {
                QuizTopBar(
                    progress = progress,
                    xp = xp,
                    onExit = onExit
                )

                Spacer(Modifier.height(20.dp))

                QuizScreenTitle(text = "Kenali Hurufnya")

                Spacer(Modifier.height(42.dp))

                Box(
                    modifier = Modifier
                        .size(200.dp)
                        .align(Alignment.CenterHorizontally)
                        .background(
                            NusalingoColors.CardYellow,
                            RoundedCornerShape(18.dp)
                        )
                        .border(
                            width = 1.dp,
                            color = Color(0xFFEBCB8F),
                            shape = RoundedCornerShape(18.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = question.character,
                        fontFamily = JavaneseFont,
                        fontSize = 80.sp,
                        color = NusalingoColors.Black,
                        textAlign = TextAlign.Center
                    )
                }

                Spacer(Modifier.height(28.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 10.dp)
                        .height(1.dp)
                        .background(Color(0xFFC3915E))
                )

                Spacer(Modifier.height(24.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    question.options.forEach { option ->
                        val answerState = when {
                            checked && option == question.answer -> QuizAnswerState.CORRECT
                            checked && option == selectedAnswer -> QuizAnswerState.WRONG
                            option == selectedAnswer -> QuizAnswerState.SELECTED
                            else -> QuizAnswerState.DEFAULT
                        }

                        AnswerOption(
                            text = option,
                            state = answerState,
                            onClick = {
                                if (!checked) selectedAnswer = option
                            },
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }
        }

        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .background(NusalingoColors.Background)
                .navigationBarsPadding()
                .padding(bottom = 12.dp)
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
                        .padding(horizontal = 20.dp, vertical = 16.dp)
                ) {
                    Text(
                        text = if (isCorrect) "Kamu Berhasil!" else "Belum tepat!",
                        fontFamily = NunitoFont,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = NusalingoColors.TextPrimary
                    )

                    if (!isCorrect) {
                        Spacer(Modifier.height(4.dp))
                        Text(
                            text = "Bacaan yang tepat: ${question.answer}",
                            fontFamily = NunitoFont,
                            fontSize = 14.sp,
                            color = NusalingoColors.TextPrimary
                        )
                    }

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
                                selectedAnswer = null
                                checked = false
                            } else {
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
                        if (selectedAnswer != null) {
                            checked = true
                            val updatedWrongQuestions = if (isCorrect) {
                                wrongQuestionIndices - currentQuestionIndex
                            } else {
                                wrongQuestionIndices + currentQuestionIndex
                            }
                            wrongQuestionIndices = updatedWrongQuestions
                            onIncorrectQuestions(updatedWrongQuestions.sorted())
                            if (isCorrect) {
                                correctAnswers++
                                confettiKey++
                            }
                        }
                    },
                    enabled = selectedAnswer != null,
                    modifier = Modifier.padding(horizontal = 20.dp)
                )
            }
        }

        if (checked && isCorrect) {
            QuizAnswerConfetti(key = confettiKey)
        }
    }
}

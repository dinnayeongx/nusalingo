package com.example.nusalingo.feature.quiz.screen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Surface
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.nusalingo.feature.lesson.audio.playAudio
import com.example.nusalingo.ui.components.PrimaryButton
import com.example.nusalingo.ui.components.quiz.QuizAnswerConfetti
import com.example.nusalingo.ui.components.quiz.QuizScreenTitle
import com.example.nusalingo.ui.components.quiz.QuizTopBar
import com.example.nusalingo.ui.components.quiz.WritingCanvas
import com.example.nusalingo.ui.theme.NusalingoColors

internal const val WRITE_CHARACTER_QUESTION_COUNT = 10
private const val XP_PER_CORRECT_ANSWER = 5

private data class WritingQuestion(
    val character: String,
    val reading: String,
    val audioName: String
)

private val writingQuestions = listOf(
    WritingQuestion("ꦲ", "Ha", "aksara_ha"),
    WritingQuestion("ꦤ", "Na", "aksara_na"),
    WritingQuestion("ꦕ", "Ca", "aksara_ca"),
    WritingQuestion("ꦫ", "Ra", "aksara_ra"),
    WritingQuestion("ꦏ", "Ka", "aksara_ka"),
    WritingQuestion("ꦢ", "Da", "aksara_da"),
    WritingQuestion("ꦠ", "Ta", "aksara_ta"),
    WritingQuestion("ꦱ", "Sa", "aksara_sa"),
    WritingQuestion("ꦮ", "Wa", "aksara_wa"),
    WritingQuestion("ꦭ", "La", "aksara_la")
)

@Composable
fun WriteCharacterScreen(
    onExit: () -> Unit,
    onFinish: (correctAnswers: Int, totalQuestions: Int, xpEarned: Int) -> Unit,
    questionIndices: List<Int> = (0 until WRITE_CHARACTER_QUESTION_COUNT).toList(),
    awardXp: Boolean = true,
    attemptId: Int = 0,
    onIncorrectQuestions: (List<Int>) -> Unit = {}
) {
    require(questionIndices.isNotEmpty()) { "At least one writing question is required." }

    val context = LocalContext.current
    val strokes = remember(questionIndices, attemptId) {
        mutableStateListOf<List<Offset>>()
    }
    var questionIndex by remember(questionIndices, attemptId) {
        mutableIntStateOf(0)
    }
    var correctAnswers by remember(questionIndices, attemptId) {
        mutableIntStateOf(0)
    }
    var checked by remember(questionIndices, attemptId) {
        mutableStateOf(false)
    }
    var answerIsCorrect by remember(questionIndices, attemptId) {
        mutableStateOf<Boolean?>(null)
    }
    var confettiKey by remember(questionIndices, attemptId) {
        mutableIntStateOf(0)
    }
    var wrongQuestionIndices by remember(questionIndices, attemptId) {
        mutableStateOf(setOf<Int>())
    }
    var canvasSize by remember(questionIndices, attemptId) {
        mutableStateOf(IntSize.Zero)
    }

    val currentQuestionIndex = questionIndices[questionIndex]
    val currentQuestion = writingQuestions[currentQuestionIndex]
    val xpEarned = if (awardXp) correctAnswers * XP_PER_CORRECT_ANSWER else 0
    val progress = (
        questionIndex + if (checked) 1 else 0
        ).toFloat() / questionIndices.size
    val density = LocalDensity.current
    val strokeWidthPx = with(density) { 10.dp.toPx() }
    val strokeColor = when (answerIsCorrect) {
        true -> Color(0xFF16AD61)
        false -> Color(0xFFD64545)
        null -> Color(0xFF1B365D)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(NusalingoColors.Background)
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
        ) {
            QuizTopBar(
                progress = progress,
                xp = xpEarned,
                onExit = onExit
            )

            Spacer(Modifier.height(20.dp))

            QuizScreenTitle(text = "Tuliskan Aksara")

            Spacer(Modifier.height(16.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = Color(0xFF3379DD),
                    modifier = Modifier.size(34.dp),
                    onClick = {
                        playAudio(context, currentQuestion.audioName)
                    }
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text("🔊", fontSize = 16.sp)
                    }
                }

                Column(
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = currentQuestion.reading,
                        fontSize = 14.sp,
                        color = NusalingoColors.TextSecondary
                    )

                    Text(
                        text = currentQuestion.character,
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Bold,
                        color = NusalingoColors.TextPrimary
                    )
                }
            }

            Spacer(Modifier.height(8.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(1f)
                        .border(
                            width = 1.dp,
                            color = NusalingoColors.InputBorder,
                            shape = RoundedCornerShape(14.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    WritingCanvas(
                        modifier = Modifier
                            .fillMaxSize()
                            .onSizeChanged { canvasSize = it },
                        strokes = strokes,
                        onStrokeAdded = { stroke -> strokes.add(stroke) },
                        guideCharacter = currentQuestion.character,
                        strokeColor = strokeColor,
                        enabled = !checked
                    )

                    Text(
                        text = "Area menulis",
                        fontSize = 12.sp,
                        color = NusalingoColors.TextSecondary.copy(alpha = 0.7f),
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .padding(bottom = 8.dp)
                    )
                }
            }

            Spacer(Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Button(
                    onClick = {
                        if (!checked && strokes.isNotEmpty()) {
                            strokes.removeAt(strokes.lastIndex)
                        }
                    },
                    enabled = !checked && strokes.isNotEmpty(),
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = NusalingoColors.SurfacePrimary,
                        contentColor = NusalingoColors.TextPrimary
                    ),
                    shape = RoundedCornerShape(16.dp),
                    border = BorderStroke(1.dp, NusalingoColors.InputBorder)
                ) {
                    Text("↶ Urungkan", fontSize = 15.sp)
                }

                Button(
                    onClick = {
                        if (!checked) strokes.clear()
                    },
                    enabled = !checked && strokes.isNotEmpty(),
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = NusalingoColors.InputBackground,
                        contentColor = NusalingoColors.TextPrimary
                    ),
                    shape = RoundedCornerShape(16.dp),
                    border = BorderStroke(1.dp, NusalingoColors.InputBorder)
                ) {
                    Text("⟲ Ulangi", fontSize = 15.sp)
                }
            }

            Spacer(Modifier.height(12.dp))
        }

        Column(
            modifier = Modifier
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
                            if (answerIsCorrect == true) {
                                NusalingoColors.GreenSecondary
                            } else {
                                Color(0xFFF4B6B6)
                            }
                        )
                        .padding(horizontal = 20.dp, vertical = 16.dp)
                ) {
                    Text(
                        text = if (answerIsCorrect == true) "Kamu Berhasil!" else "Belum tepat!",
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
                        containerColor = if (answerIsCorrect == true) {
                            Color(0xFF16AD61)
                        } else {
                            Color(0xFFD64545)
                        },
                        onClick = {
                            if (questionIndex < questionIndices.lastIndex) {
                                questionIndex++
                                strokes.clear()
                                checked = false
                                answerIsCorrect = null
                            } else {
                                onIncorrectQuestions(wrongQuestionIndices.sorted())
                                onFinish(
                                    correctAnswers,
                                    questionIndices.size,
                                    xpEarned
                                )
                            }
                        }
                    )
                }
            } else {
                PrimaryButton(
                    text = "CEK",
                    onClick = {
                        if (strokes.isNotEmpty()) {
                            val isCorrect = AksaraDrawingEvaluator.isSimilar(
                                strokes = strokes.toList(),
                                character = currentQuestion.character,
                                width = canvasSize.width,
                                height = canvasSize.height,
                                strokeWidthPx = strokeWidthPx
                            )
                            checked = true
                            answerIsCorrect = isCorrect
                            if (isCorrect) {
                                correctAnswers++
                                confettiKey++
                            } else {
                                wrongQuestionIndices =
                                    wrongQuestionIndices + currentQuestionIndex
                            }
                        }
                    },
                    enabled = strokes.isNotEmpty() &&
                        canvasSize.width > 0 &&
                        canvasSize.height > 0,
                    modifier = Modifier.padding(horizontal = 20.dp)
                )
            }
        }
    }

    if (checked && answerIsCorrect == true) {
        QuizAnswerConfetti(key = confettiKey)
    }
}

package com.example.nusalingo.feature.quiz.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.platform.LocalContext
import com.example.nusalingo.feature.lesson.audio.AudioSequencePlayer
import com.example.nusalingo.ui.components.PrimaryButton
import com.example.nusalingo.ui.components.quiz.AnswerOption
import com.example.nusalingo.ui.components.quiz.BubbleQuestion
import com.example.nusalingo.ui.components.quiz.BubbleType
import com.example.nusalingo.ui.components.quiz.QuizAnswerConfetti
import com.example.nusalingo.ui.components.quiz.QuizAnswerState
import com.example.nusalingo.ui.components.quiz.QuizScreenTitle
import com.example.nusalingo.ui.components.quiz.QuizTopBar
import com.example.nusalingo.ui.theme.NunitoFont
import com.example.nusalingo.ui.theme.NusalingoColors

internal const val AUDIO_QUIZ_QUESTION_COUNT = 10
private const val XP_PER_CORRECT_ANSWER = 5

private data class AudioQuestion(
    val script: String,
    val audioResourceNames: List<String>,
    val answer: String,
    val options: List<String>
)

private val AudioQuestions = listOf(
    AudioQuestion(
        script = "ꦫꦩ ꦤꦠ ꦧꦠ",
        audioResourceNames = listOf(
            "aksara_ra", "aksara_ma", "aksara_na", "aksara_ta", "aksara_ba", "aksara_ta"
        ),
        answer = "Rama nata bata",
        options = listOf("Rama nata bata", "Rama nata rata", "Rama tata bata")
    ),
    AudioQuestion(
        script = "ꦏꦶꦏꦶ ꦭꦫ",
        audioResourceNames = listOf("swara_ki", "swara_ki", "aksara_la", "aksara_ra"),
        answer = "Kiki lara",
        options = listOf("Kiki lara", "Kéke lara", "Koko lara")
    ),
    AudioQuestion(
        script = "ꦏꦼꦥꦭ ꦱꦥꦶ",
        audioResourceNames = listOf(
            "swara_ke_pepet", "aksara_pa", "aksara_la", "kata_sapi"
        ),
        answer = "Kêpala sapi",
        options = listOf("Kêpala sapi", "Képala sapi", "Kêpala sapa")
    ),
    AudioQuestion(
        script = "ꦧꦱ ꦗꦮ",
        audioResourceNames = listOf("aksara_ba", "aksara_sa", "aksara_ja", "aksara_wa"),
        answer = "Basa Jawa",
        options = listOf("Rasa Jawa", "Basa Jawi", "Basa Jawa")
    ),
    AudioQuestion(
        script = "ꦱꦥ ꦫꦩ",
        audioResourceNames = listOf("kata_sapa", "aksara_ra", "aksara_ma"),
        answer = "Sapa Rama",
        options = listOf("Sapa Rara", "Sapa Rama", "Sapi Rama")
    ),
    AudioQuestion(
        script = "ꦏꦺꦏꦺ ꦭꦫ",
        audioResourceNames = listOf(
            "swara_ke_taling", "swara_ke_taling", "aksara_la", "aksara_ra"
        ),
        answer = "Kéke lara",
        options = listOf("Kiki lara", "Kêke lara", "Kéke lara")
    ),
    AudioQuestion(
        script = "ꦧꦶꦧꦶ ꦧꦸꦏ ꦧꦸꦏꦸ",
        audioResourceNames = listOf("kata_bibi", "kata_buka", "kata_buku"),
        answer = "Bibi buka buku",
        options = listOf("Bibi buka bibi", "Bibi buka buku", "Bibi buka buka")
    ),
    AudioQuestion(
        script = "ꦫꦩ ꦚꦥ ꦤꦤ",
        audioResourceNames = listOf(
            "aksara_ra", "aksara_ma", "aksara_nya", "aksara_pa", "aksara_na", "aksara_na"
        ),
        answer = "Rama nyapa Nana",
        options = listOf("Rama nyapa Nana", "Rama nyapa Rara", "Nana nyapa Rama")
    ),
    AudioQuestion(
        script = "ꦫꦫ ꦚꦥ ꦲꦫꦶ",
        audioResourceNames = listOf(
            "aksara_ra", "aksara_ra", "aksara_nya", "aksara_pa", "kata_hari"
        ),
        answer = "Rara nyapa Hari",
        options = listOf("Rama nyapa Hari", "Rara nyapa Hari", "Rara nyapa Hara")
    ),
    AudioQuestion(
        script = "ꦏꦺꦴꦏꦺꦴ ꦭꦫ",
        audioResourceNames = listOf("swara_ko", "swara_ko", "aksara_la", "aksara_ra"),
        answer = "Koko lara",
        options = listOf("Koko lara", "Kéke lara", "Kiki lara")
    )
)

@Composable
fun AudioQuizScreen(
    onExit: () -> Unit,
    onFinish: (
        correctAnswers: Int,
        totalQuestions: Int,
        xpEarned: Int
    ) -> Unit,
    questionIndices: List<Int> = (0 until AUDIO_QUIZ_QUESTION_COUNT).toList(),
    awardXp: Boolean = true,
    attemptId: Int = 0,
    onIncorrectQuestions: (List<Int>) -> Unit = {},
    progressOverride: Float? = null,
    xpOverride: Int? = null
) {
    require(questionIndices.isNotEmpty()) { "At least one listening question is required." }

    val context = LocalContext.current
    val audioPlayer = remember(context) { AudioSequencePlayer(context) }
    val scrollState = rememberScrollState()
    var questionIndex by remember(questionIndices, attemptId) { mutableIntStateOf(0) }
    var selectedAnswer by remember(questionIndices, attemptId) { mutableStateOf<String?>(null) }
    var checked by remember(questionIndices, attemptId) { mutableStateOf(false) }
    var correctAnswers by remember(questionIndices, attemptId) { mutableIntStateOf(0) }
    var confettiKey by remember(questionIndices, attemptId) { mutableIntStateOf(0) }
    var wrongQuestionIndices by remember(questionIndices, attemptId) {
        mutableStateOf(setOf<Int>())
    }
    var hasPlayedAudio by remember(questionIndices, attemptId, questionIndex) {
        mutableStateOf(false)
    }

    DisposableEffect(audioPlayer, questionIndex, attemptId) {
        onDispose { audioPlayer.stop() }
    }

    val currentQuestionIndex = questionIndices[questionIndex]
    val question = AudioQuestions[currentQuestionIndex]
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
                .verticalScroll(scrollState)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
            ) {
                QuizTopBar(
                    progress = progressOverride ?: progress,
                    xp = xpOverride ?: xp,
                    onExit = {
                        audioPlayer.stop()
                        onExit()
                    }
                )

                Spacer(Modifier.height(20.dp))

                QuizScreenTitle(text = "Uji Pendengaran", fontSize = 22.sp)

                Spacer(Modifier.height(8.dp))

                Text(
                    text = "Dengarkan frasa, lalu pilih bacaan yang tepat.",
                    fontFamily = NunitoFont,
                    fontSize = 14.sp,
                    color = NusalingoColors.TextSecondary
                )

                Spacer(Modifier.height(32.dp))

                BubbleQuestion(
                    text = question.script,
                    type = BubbleType.AKSARA_AUDIO,
                    onAudioClick = {
                        audioPlayer.play(question.audioResourceNames)
                        hasPlayedAudio = true
                    }
                )

                Spacer(Modifier.height(32.dp))

                Text(
                    text = "Pilih bacaan yang kamu dengar",
                    fontFamily = NunitoFont,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = NusalingoColors.TextPrimary
                )

                Spacer(Modifier.height(12.dp))

                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
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
                            modifier = Modifier.fillMaxWidth()
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
                            audioPlayer.stop()
                            if (questionIndex < questionIndices.lastIndex) {
                                questionIndex++
                                selectedAnswer = null
                                checked = false
                            } else {
                                onIncorrectQuestions(wrongQuestionIndices.sorted())
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
                        if (selectedAnswer != null && hasPlayedAudio) {
                            audioPlayer.stop()
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
                    enabled = selectedAnswer != null && hasPlayedAudio,
                    modifier = Modifier.padding(horizontal = 20.dp)
                )
            }
        }

        if (checked && isCorrect) {
            QuizAnswerConfetti(key = confettiKey)
        }
    }
}

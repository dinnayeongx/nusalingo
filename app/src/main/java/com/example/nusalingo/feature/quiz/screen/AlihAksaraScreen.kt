package com.example.nusalingo.feature.quiz.screen

import androidx.compose.runtime.Composable
import com.example.nusalingo.feature.quiz.QuizAksara
import com.example.nusalingo.feature.quiz.QuizAksaraContent
import com.example.nusalingo.ui.components.quiz.BubbleType

internal const val ALIH_AKSARA_QUESTION_COUNT = 10

private val AlihAksaraQuestions = listOf(
    QuizAksara(
        bubble = "ꦲꦤꦏꦕꦩꦠ",
        answer = listOf("Ana", "Kacamata"),
        bank = listOf("Ana", "Laksamana", "Nata", "Pada", "Kacamata")
    ),
    QuizAksara(
        bubble = "ꦧꦭꦫꦩ",
        answer = listOf("Bala", "Rama"),
        bank = listOf("Rama", "Nama", "Bala", "Cara")
    ),
    QuizAksara(
        bubble = "ꦧꦱꦗꦮ",
        answer = listOf("Basa", "Jawa"),
        bank = listOf("Jawa", "Sapa", "Basa", "Nata")
    ),
    QuizAksara(
        bubble = "ꦧꦸꦏꦸꦧꦫꦸ",
        answer = listOf("Buku", "Baru"),
        bank = listOf("Baru", "Buku", "Buru", "Bala")
    ),
    QuizAksara(
        bubble = "ꦲꦤꦱꦥꦶ",
        answer = listOf("Ana", "Sapi"),
        bank = listOf("Ana", "Sapi", "Sapa", "Bala")
    ),
    QuizAksara(
        bubble = "ꦫꦩꦩꦕ",
        answer = listOf("Rama", "Maca"),
        bank = listOf("Maca", "Rama", "Mata", "Cara")
    ),
    QuizAksara(
        bubble = "ꦱꦥꦶꦧꦫꦸ",
        answer = listOf("Sapi", "Baru"),
        bank = listOf("Baru", "Sapi", "Buku", "Nama")
    ),
    QuizAksara(
        bubble = "ꦏꦕꦧꦫꦸ",
        answer = listOf("Kaca", "Baru"),
        bank = listOf("Kaca", "Baru", "Mata", "Rama")
    ),
    QuizAksara(
        bubble = "ꦧꦭꦧꦫꦸ",
        answer = listOf("Bala", "Baru"),
        bank = listOf("Bala", "Baru", "Buku", "Sapi")
    ),
    QuizAksara(
        bubble = "ꦤꦩꦫꦩ",
        answer = listOf("Nama", "Rama"),
        bank = listOf("Nama", "Rama", "Nata", "Basa")
    )
)

@Composable
fun AlihAksaraScreen(
    onExit: () -> Unit,
    onFinish: (
        correctAnswers: Int,
        totalQuestions: Int,
        xpEarned: Int
    ) -> Unit,
    onWrongAnswers: (List<QuizReviewAnswer>) -> Unit = {},
    questionIndices: List<Int> = AlihAksaraQuestions.indices.toList(),
    awardXp: Boolean = true,
    attemptId: Int = 0,
    progressOverride: Float? = null,
    xpOverride: Int? = null
) {
    QuizAksaraContent(
        questions = AlihAksaraQuestions,
        bubbleType = BubbleType.AKSARA_LARGE,
        tokensAreAksara = false,
        onExit = onExit,
        onFinish = onFinish,
        xpPerCorrectAnswer = 5,
        celebrateCorrectAnswers = true,
        questionIndices = questionIndices,
        awardXp = awardXp,
        attemptId = attemptId,
        progressOverride = progressOverride,
        xpOverride = xpOverride,
        onIncorrectQuestionIndices = { indices ->
            onWrongAnswers(
                indices.map { index ->
                    val question = AlihAksaraQuestions[index]
                    QuizReviewAnswer(
                        questionNumber = index + 1,
                        prompt = question.bubble,
                        correctAnswer = question.answer.joinToString(" ")
                    )
                }
            )
        }
    )
}
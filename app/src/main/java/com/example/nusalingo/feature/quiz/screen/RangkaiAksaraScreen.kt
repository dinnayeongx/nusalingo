package com.example.nusalingo.feature.quiz.screen

import androidx.compose.runtime.Composable
import com.example.nusalingo.feature.quiz.QuizAksara
import com.example.nusalingo.feature.quiz.QuizAksaraContent
import com.example.nusalingo.ui.components.quiz.BubbleType

internal const val RANGKAI_AKSARA_QUESTION_COUNT = 10

private fun rangkaiQuestion(
    prompt: String,
    answer: List<String>,
    distractors: List<String>
) = QuizAksara(
    bubble = prompt,
    answer = answer,
    bank = (answer + distractors).shuffled()
)

private val RangkaiAksaraQuestions = listOf(
    rangkaiQuestion(
        prompt = "SAPI TURU",
        answer = listOf("ꦱ", "ꦥꦶ", "ꦠꦸ", "ꦫꦸ"),
        distractors = listOf("ꦩ", "ꦫ", "ꦧ")
    ),
    rangkaiQuestion(
        prompt = "SEGO SATE ENAK",
        answer = listOf("ꦱꦼ", "ꦒꦺꦴ", "ꦱ", "ꦠꦺ", "ꦲꦺ", "ꦤ", "ꦏ꧀"),
        distractors = listOf("ꦧꦸ", "ꦭ", "ꦥꦶ")
    ),
    rangkaiQuestion(
        prompt = "PITIK TELU",
        answer = listOf("ꦥꦶ", "ꦠꦶ", "ꦏ꧀", "ꦠꦼ", "ꦭꦸ"),
        distractors = listOf("ꦱ", "ꦧ", "ꦫꦸ")
    ),
    rangkaiQuestion(
        prompt = "KOTA ANYAR",
        answer = listOf("ꦏꦺꦴ", "ꦠ", "ꦲ", "ꦚꦂ"),
        distractors = listOf("ꦧꦸ", "ꦫ", "ꦱ")
    ),
    rangkaiQuestion(
        prompt = "TELO IKI ENAK",
        answer = listOf("ꦠꦼ", "ꦭꦺꦴ", "ꦲꦶ", "ꦏꦶ", "ꦲꦺ", "ꦤ", "ꦏ꧀"),
        distractors = listOf("ꦥꦶ", "ꦱꦼ", "ꦩ")
    ),
    rangkaiQuestion(
        prompt = "KULI MACA BUKU",
        answer = listOf("ꦏꦸ", "ꦭꦶ", "ꦩ", "ꦕ", "ꦧꦸ", "ꦏꦸ"),
        distractors = listOf("ꦱꦼ", "ꦏ꧀", "ꦫꦸ")
    ),
    rangkaiQuestion(
        prompt = "BALA RAMA",
        answer = listOf("ꦧ", "ꦭ", "ꦫ", "ꦩ"),
        distractors = listOf("ꦤ", "ꦱ", "ꦏ")
    ),
    rangkaiQuestion(
        prompt = "PISO ANYAR",
        answer = listOf("ꦥꦶ", "ꦱꦺꦴ", "ꦲ", "ꦚꦂ"),
        distractors = listOf("ꦧꦸ", "ꦧ", "ꦭꦶ")
    ),
    rangkaiQuestion(
        prompt = "BOCAH MACA CERITA",
        answer = listOf("ꦧꦺꦴ", "ꦕꦃ", "ꦩ", "ꦕ", "ꦕꦼ", "ꦫꦶ", "ꦠ"),
        distractors = listOf("ꦱꦼ", "ꦏꦸ", "ꦥꦶ")
    ),
    rangkaiQuestion(
        prompt = "ANA BEBEK ING KOTA ANYAR",
        answer = listOf(
            "ꦲ", "ꦤ", "ꦧꦼ", "ꦧꦼ", "ꦏ꧀", "ꦲꦶꦁ", "ꦏꦺꦴ", "ꦠ", "ꦲ", "ꦚꦂ"
        ),
        distractors = listOf("ꦥꦶ", "ꦭꦶ", "ꦱꦼ", "ꦒꦺꦴ")
    )
)

@Composable
fun RangkaiAksaraScreen(
    onExit: () -> Unit,
    onFinish: (
        correctAnswers: Int,
        totalQuestions: Int,
        xpEarned: Int
    ) -> Unit,
    onWrongAnswers: (List<QuizReviewAnswer>) -> Unit = {},
    questionIndices: List<Int> = RangkaiAksaraQuestions.indices.toList(),
    awardXp: Boolean = true,
    attemptId: Int = 0,
    progressOverride: Float? = null,
    xpOverride: Int? = null
) {
    QuizAksaraContent(
        questions = RangkaiAksaraQuestions,
        bubbleType = BubbleType.LATIN_TEXT,
        tokensAreAksara = true,
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
                    val question = RangkaiAksaraQuestions[index]
                    QuizReviewAnswer(
                        questionNumber = index + 1,
                        prompt = question.bubble,
                        correctAnswer = question.answer.joinToString("")
                    )
                }
            )
        }
    )
}

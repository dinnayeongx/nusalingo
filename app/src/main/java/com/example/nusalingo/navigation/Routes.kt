package com.example.nusalingo.navigation
import android.net.Uri

object Routes {
    const val LOGIN = "auth/login"
    const val LOGIN_LANDING = "auth/login_landing"
    const val REGISTER = "auth/register"
    const val LANDING = "auth/landing/{displayName}"
    const val ONBOARDING = "auth/onboarding/{displayName}"
    const val FORGOT_PASSWORD = "auth/forgot_password"
    const val CHECK_EMAIL = "auth/check_email"

    const val HOME = "home"
    const val SELECT_SCRIPT = "select_script"
    const val LEARN = "learn"
    const val LESSON_DETAIL = "lesson_detail/{title}"
    const val PRACTICE = "practice"

    const val LEVEL_SESSION = "level/{levelId}"
    const val PRACTICE_QUIZ = "practice/quiz/{quizType}"

    fun levelSession(levelId: Int) = "level/$levelId"
    fun practiceQuiz(quizType: String) = "practice/quiz/$quizType"
    fun lessonDetail(title: String) = "lesson_detail/${Uri.encode(title)}"
    fun landing(displayName: String) = "auth/landing/${android.net.Uri.encode(displayName)}"
    fun onboarding(displayName: String) = "auth/onboarding/${android.net.Uri.encode(displayName)}"

    val authRoutes = setOf(
        LOGIN,
        LOGIN_LANDING,
        REGISTER,
        LANDING,
        ONBOARDING,
        FORGOT_PASSWORD,
        CHECK_EMAIL,
    )
}
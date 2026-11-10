package com.example.nusalingo.navigation

object Routes {
    const val LOGIN = "auth/login"
    const val FORGOT_PASSWORD = "auth/forgot_password"
    const val CHECK_EMAIL = "auth/check_email"
    const val RESET_PASSWORD = "auth/reset_password"
    const val RESET_SUCCESS = "auth/reset_success"

    const val HOME = "home"
    const val SELECT_SCRIPT = "select_script"
    const val LEARN = "learn"
    const val LESSON_DETAIL = "lesson_detail/{lessonId}"
    const val PRACTICE = "practice"

    const val LEVEL_SESSION = "level/{levelId}"
    const val PRACTICE_QUIZ = "practice/quiz/{quizType}"

    fun levelSession(levelId: Int) = "level/$levelId"
    fun practiceQuiz(quizType: String) = "practice/quiz/$quizType"
    fun lessonDetail(lessonId: Int) = "lesson_detail/$lessonId"

    val authRoutes = setOf(LOGIN, FORGOT_PASSWORD, CHECK_EMAIL, RESET_PASSWORD, RESET_SUCCESS)
}
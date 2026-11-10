package com.example.nusalingo.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.nusalingo.feature.home.screen.HomeRoute
import com.example.nusalingo.feature.home.screen.PilihAksaraRoute
import com.example.nusalingo.feature.lesson.screen.LessonDetailRoute
import com.example.nusalingo.feature.lesson.screen.LessonRoute
import com.example.nusalingo.feature.practice.screen.PracticeRoute
import com.example.nusalingo.feature.quiz.screen.QuizFlowRoute
import com.example.nusalingo.feature.quiz.screen.QuizType

@Composable
fun NusalingoNavigation(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    startDestination: String = Routes.LOGIN,
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        authNavGraph(navController)

        composable(Routes.HOME) {
            HomeRoute(
                onExplore = { navController.navigate(Routes.SELECT_SCRIPT) },
                onLevel = { levelId -> navController.navigate(Routes.levelSession(levelId)) }
            )
        }

        composable(Routes.SELECT_SCRIPT) {
            PilihAksaraRoute(
                onBack = { navController.popBackStack() },
                onSelect = { navController.navigate(Routes.levelSession(1)) }
            )
        }

        composable(Routes.LEARN) {
            LessonRoute(
                onLessonSelected = { lessonTitle ->
                    navController.navigate(Routes.lessonDetail(1))
                }
            )
        }

        composable(Routes.LESSON_DETAIL) {
            LessonDetailRoute(
                onBack = { navController.popBackStack() },
                onStartLesson = { /* Alur memulai materi */ }
            )
        }

        composable(Routes.PRACTICE) {
            PracticeRoute(
                onPractice = { quizType ->
                    navController.navigate(Routes.practiceQuiz(quizType.name))
                }
            )
        }

        composable(Routes.PRACTICE_QUIZ) { backStackEntry ->
            val quizTypeName = backStackEntry.arguments?.getString("quizType")
            val quizType = try {
                QuizType.valueOf(quizTypeName ?: "")
            } catch (e: Exception) {
                QuizType.ALIH_AKSARA
            }

            QuizFlowRoute(
                quizType = quizType,
                onNavigateBack = { navController.popBackStack() }
            )
        }
    }
}
package com.example.nusalingo.navigation

import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.NavType
import androidx.navigation.navArgument
import androidx.navigation.compose.composable
import com.example.nusalingo.feature.home.screen.HomeRoute
import com.example.nusalingo.feature.home.screen.PilihAksaraRoute
import com.example.nusalingo.feature.level.screen.LevelQuizRoute
import com.example.nusalingo.feature.lesson.screen.LessonDetailRoute
import com.example.nusalingo.feature.lesson.screen.LessonRoute
import com.example.nusalingo.feature.practice.screen.PracticeRoute
import com.example.nusalingo.feature.profile.AchievementScreen
import com.example.nusalingo.feature.profile.ProfileRoute
import com.example.nusalingo.feature.profile.ProfileViewModel
import com.example.nusalingo.feature.profile.ProfileViewModelFactory
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
        modifier = modifier,
        enterTransition = {
            fadeIn(tween(420)) + slideInHorizontally(
                initialOffsetX = { it / 12 },
                animationSpec = tween(420),
            )
        },
        exitTransition = {
            fadeOut(tween(260)) + slideOutHorizontally(
                targetOffsetX = { -it / 16 },
                animationSpec = tween(260),
            )
        },
        popEnterTransition = {
            fadeIn(tween(360)) + slideInHorizontally(
                initialOffsetX = { -it / 16 },
                animationSpec = tween(360),
            )
        },
        popExitTransition = {
            fadeOut(tween(240)) + slideOutHorizontally(
                targetOffsetX = { it / 16 },
                animationSpec = tween(240),
            )
        },
    ) {
        authNavGraph(navController)

        composable(Routes.HOME) {
            HomeRoute(
                onExplore = { navController.navigate(Routes.SELECT_SCRIPT) },
                onLevel = { levelId -> navController.navigate(Routes.levelSession(levelId)) }
            )
        }

        composable(
            route = Routes.LEVEL_SESSION,
            arguments = listOf(navArgument("levelId") { type = NavType.IntType })
        ) { backStackEntry ->
            val levelId = backStackEntry.arguments?.getInt("levelId") ?: 1
            LevelQuizRoute(
                levelId = levelId,
                onBack = { navController.popBackStack() }
            )
        }


        composable(Routes.SELECT_SCRIPT) {
            PilihAksaraRoute(
                onBack = {
                    navController.popBackStack()
                },
                onSelect = {
                    navController.navigate(Routes.HOME) {
                        popUpTo(Routes.HOME) {
                            inclusive = false
                        }
                        launchSingleTop = true
                    }
                }
            )
        }

        composable(Routes.LEARN) {
            LessonRoute(
                onLessonSelected = { lessonTitle ->
                    navController.navigate(
                        Routes.lessonDetail(lessonTitle)
                    )
                }
            )
        }

        composable(
            route = Routes.LESSON_DETAIL,
            arguments = listOf(navArgument("title") { type = NavType.StringType })
        ) { backStackEntry ->
            val title = backStackEntry.arguments?.getString("title") ?: ""

            LessonDetailRoute(
                lessonTitle = title,
                onBack = { navController.popBackStack() }
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

        composable(Routes.PROFILE) {
            ProfileRoute(
                onNavigateToAchievement = { navController.navigate(Routes.ACHIEVEMENT) },
                onLoggedOut = {
                    navController.navigate(Routes.LOGIN) {
                        popUpTo(navController.graph.id) { inclusive = true }
                        launchSingleTop = true
                    }
                }
            )
        }

        composable(Routes.ACHIEVEMENT) {
            val context = LocalContext.current.applicationContext
            val vm: ProfileViewModel = viewModel(factory = ProfileViewModelFactory(context))
            val state by vm.uiState.collectAsState()
            AchievementScreen(
                achievements = state.achievements,
                onBackClick = { navController.popBackStack() }
            )
        }
    }
}

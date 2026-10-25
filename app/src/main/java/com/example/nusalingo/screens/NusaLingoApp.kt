package com.example.nusalingo.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.example.nusalingo.navigation.MainTab
import com.example.nusalingo.navigation.NusaBottomNavigation
import com.example.nusalingo.screens.home.HomeScreen
import com.example.nusalingo.screens.home.PilihAksaraScreen
import com.example.nusalingo.screens.lesson.LessonDetailScreen
import com.example.nusalingo.screens.lesson.LessonScreen
import com.example.nusalingo.screens.practice.PracticeScreen
import com.example.nusalingo.screens.quiz.QuizFlowScreen
import com.example.nusalingo.screens.quiz.QuizType
import com.example.nusalingo.ui.theme.NusalingoColors
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.nusalingo.navigation.NusaBottomNavigation
import com.example.nusalingo.navigation.NusalingoNavigation
import com.example.nusalingo.navigation.Routes

private enum class AppPage { HOME, PRACTICE, LESSON, LESSON_DETAIL, SCRIPT_SELECT }

@Composable
fun NusaLingoApp() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val currentTab = when (currentRoute) {
        Routes.HOME -> MainTab.HOME
        Routes.PRACTICE -> MainTab.PRACTICE
        Routes.LEARN -> MainTab.LESSON
        else -> MainTab.HOME
    }

    Scaffold(
        containerColor = NusalingoColors.Background,
        bottomBar = {
            if (currentRoute?.startsWith("practice/quiz") != true) {
                NusaBottomNavigation(
                    selected = currentTab,
                    onSelect = { tab ->
                        val destination = when (tab) {
                            MainTab.HOME -> Routes.HOME
                            MainTab.PRACTICE -> Routes.PRACTICE
                            MainTab.LESSON -> Routes.LEARN
                            MainTab.PROFILE -> Routes.HOME
                        }
                        navController.navigate(destination) {
                            popUpTo(Routes.HOME) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )
            }
        }
    ) { paddingValues ->
        NusalingoNavigation(
            navController = navController,
            modifier = Modifier.padding(paddingValues)
        )
    }
}

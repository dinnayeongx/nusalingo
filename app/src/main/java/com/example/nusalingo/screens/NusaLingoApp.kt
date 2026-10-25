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

private enum class AppPage { HOME, PRACTICE, LESSON, LESSON_DETAIL, SCRIPT_SELECT }

@Composable
fun NusaLingoApp() {
    var page by remember { mutableStateOf(AppPage.HOME) }
    var tab by remember { mutableStateOf(MainTab.HOME) }
    var selectedQuiz by remember { mutableStateOf<QuizType?>(null) }
    var selectedLevel by remember { mutableIntStateOf(1) }
    var detailBackPage by remember { mutableStateOf(AppPage.HOME) }

    var selectedLessonTitle by remember {
        mutableStateOf("Carakan 1")
    }

    BackHandler(enabled = selectedQuiz != null) { selectedQuiz = null }
    BackHandler(enabled = selectedQuiz == null && page != AppPage.HOME) {
        page = when (page) {
            AppPage.SCRIPT_SELECT -> AppPage.HOME
            AppPage.LESSON_DETAIL -> detailBackPage
            else -> AppPage.HOME
        }
    }
    if (selectedQuiz != null) {
        QuizFlowScreen(quizType = selectedQuiz!!)
        return
    }
    Scaffold(
        containerColor = NusalingoColors.Background,
        bottomBar = {
            NusaBottomNavigation(selected = tab) { chosen ->
                tab = chosen
                page = when (chosen) {
                    MainTab.HOME -> AppPage.HOME
                    MainTab.PRACTICE -> AppPage.PRACTICE
                    MainTab.LESSON -> AppPage.LESSON
                    MainTab.PROFILE -> AppPage.HOME
                }
            }
        }
    ) { padding ->
        Box(Modifier.fillMaxSize().padding(padding)) {
            when (page) {
                AppPage.HOME -> HomeScreen(
                    onExplore = { page = AppPage.SCRIPT_SELECT },
                    onLevel = { selectedLevel = it; detailBackPage = AppPage.HOME; page = AppPage.LESSON_DETAIL }
                )
                AppPage.PRACTICE -> PracticeScreen(onPractice = { selectedQuiz = it })
                AppPage.LESSON -> LessonScreen(
                    onLessonSelected = { lessonTitle ->
                        selectedLessonTitle = lessonTitle
                        page = AppPage.LESSON_DETAIL
                    }
                )
                AppPage.LESSON_DETAIL -> LessonDetailScreen(
                    title = selectedLessonTitle,
                    onBack = { page = AppPage.LESSON },
                    onStartLesson = { /* TODO: connect to lesson flow */ }
                )
                AppPage.SCRIPT_SELECT -> PilihAksaraScreen(
                    onBack = { page = AppPage.HOME },
                    onSelect = { selectedLevel = 1; detailBackPage = AppPage.HOME; page = AppPage.LESSON_DETAIL }
                )
            }
        }
    }
}

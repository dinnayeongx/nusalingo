package com.example.nusalingo.screens.lesson

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class LessonViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(LessonState())
    val uiState: StateFlow<LessonState> = _uiState.asStateFlow()

    init {
        loadLessons()
    }

    private fun loadLessons() {
        _uiState.update { currentState ->
            currentState.copy(
                lessonList = listOf(
                    "Carakan 1",
                    "Carakan 2",
                    "Carakan 3",
                    "Sandhangan Swara",
                    "Sandhangan Panyigeg Wanda",
                    "Merangkai Kata"
                ),
                currentDetail = getDetailForLesson("Carakan 1")
            )
        }
    }

    fun selectLesson(title: String) {
        _uiState.update { currentState ->
            currentState.copy(
                currentDetail = getDetailForLesson(title)
            )
        }
    }

    private fun getDetailForLesson(title: String): LessonDetail {
        val carakanSection = ScriptChartGroup(
            title = "AKSARA CARAKAN (nglegena)",
            rows = listOf(
                listOf("ꦲ" to "ha", "ꦤ" to "na", "ꦕ" to "ca", "ꦫ" to "ra", "ꦏ" to "ka"),
                listOf("ꦢ" to "da", "ꦠ" to "ta", "ꦱ" to "sa", "ꦮ" to "wa", "ꦭ" to "la"),
                listOf("ꦥ" to "pa", "ꦝ" to "dha", "ꦗ" to "ja", "ꦪ" to "ya", "ꦚ" to "nya"),
                listOf("ꦩ" to "ma", "ꦒ" to "ga", "ꦧ" to "ba", "ꦛ" to "tha", "ꦔ" to "nga")
            )
        )

        val pasanganSection = ScriptChartGroup(
            title = "AKSARA PASANGAN (mati)",
            rows = listOf(
                listOf("꧀ꦲ" to "h", "꧀ꦤ" to "n", "꧀ꦕ" to "c", "꧀ꦫ" to "r", "꧀ꦏ" to "k"),
                listOf("꧀ꦢ" to "d", "꧀ꦠ" to "t", "꧀ꦱ" to "s", "꧀ꦮ" to "w", "꧀ꦭ" to "l")
            )
        )

        return LessonDetail(
            title = title,
            description = "Kenali sistem penulisan tradisional dan aksara utama dalam budaya Jawa",
            chartSections = listOf(carakanSection, pasanganSection)
        )
    }
}
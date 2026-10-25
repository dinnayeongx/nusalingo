package com.example.nusalingo.screens.practice

import androidx.lifecycle.ViewModel
import com.example.nusalingo.screens.quiz.QuizType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class PracticeViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(PracticeState())
    val uiState: StateFlow<PracticeState> = _uiState.asStateFlow()

    init {
        loadPracticeItems()
    }

    private fun loadPracticeItems() {
        _uiState.update { currentState ->
            currentState.copy(
                practiceItems = listOf(
                    PracticeItem("Pasangkan Aksara", QuizType.MATCHING_PAIRS),
                    PracticeItem("Alih Aksara", QuizType.ALIH_AKSARA),
                    PracticeItem("Gores Aksara", QuizType.WRITE_CHARACTER),
                    PracticeItem("Rangkai Aksara", QuizType.RANGKAI_AKSARA),
                    PracticeItem("Tebak Aksara", QuizType.MULTIPLE_CHOICE),
                    PracticeItem("Uji Pendengaran", QuizType.AUDIO_QUIZ)
                )
            )
        }
    }
}
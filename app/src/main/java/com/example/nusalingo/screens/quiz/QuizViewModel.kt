package com.example.nusalingo.screens.quiz

import androidx.compose.ui.geometry.Offset
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class QuizViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(QuizState())
    val uiState: StateFlow<QuizState> = _uiState.asStateFlow()

    fun startQuiz(type: QuizType, questionCount: Int = 5) {
        _uiState.value = QuizState(
            quizType = type,
            totalQuestions = questionCount
        )
    }

    // --- Logika Pilihan Ganda & Balok ---
    fun selectOption(option: String) {
        if (!_uiState.value.isChecked) {
            _uiState.update { it.copy(selectedOption = option) }
        }
    }

    fun toggleTokenPlacement(index: Int, maxSlots: Int) {
        if (_uiState.value.isChecked) return

        _uiState.update { state ->
            val currentPlaced = state.placedTokenIndices.toMutableList()
            if (currentPlaced.contains(index)) {
                currentPlaced.remove(index)
            } else if (currentPlaced.size < maxSlots) {
                currentPlaced.add(index)
            }
            state.copy(placedTokenIndices = currentPlaced)
        }
    }

    fun removeTokenAtSlot(slotIndex: Int) {
        if (_uiState.value.isChecked) return
        _uiState.update { state ->
            val currentPlaced = state.placedTokenIndices.toMutableList()
            if (slotIndex in currentPlaced.indices) {
                currentPlaced.removeAt(slotIndex)
            }
            state.copy(placedTokenIndices = currentPlaced)
        }
    }

    // --- Logika Matching Pairs ---
    fun selectMatchingCharacter(index: Int) {
        if (!_uiState.value.isChecked && index !in _uiState.value.matchedPairIndices) {
            _uiState.update { it.copy(selectedCharacterIndex = index) }
        }
    }

    fun selectMatchingReading(index: Int) {
        if (!_uiState.value.isChecked) {
            _uiState.update { it.copy(selectedReadingIndex = index) }
        }
    }

    fun checkMatchingPair(isCorrect: Boolean) {
        _uiState.update { state ->
            val newMatched = if (isCorrect && state.selectedCharacterIndex != null) {
                state.matchedPairIndices + state.selectedCharacterIndex
            } else {
                state.matchedPairIndices
            }
            val newCorrect = if (isCorrect) state.correctAnswers + 1 else state.correctAnswers

            state.copy(
                isChecked = true,
                isLastPairCorrect = isCorrect,
                matchedPairIndices = newMatched,
                correctAnswers = newCorrect,
                xpEarned = newCorrect * 10
            )
        }
    }

    // --- Logika Gores Aksara ---
    fun addStroke(stroke: List<Offset>) {
        _uiState.update { state ->
            state.copy(strokes = state.strokes + listOf(stroke))
        }
    }

    fun undoStroke() {
        _uiState.update { state ->
            if (state.strokes.isNotEmpty()) {
                state.copy(strokes = state.strokes.dropLast(1))
            } else state
        }
    }

    fun clearStrokes() {
        _uiState.update { it.copy(strokes = emptyList()) }
    }

    // --- General Flow Controls ---
    fun submitAnswer(isCorrect: Boolean) {
        _uiState.update { state ->
            val newCorrect = if (isCorrect) state.correctAnswers + 1 else state.correctAnswers
            state.copy(
                isChecked = true,
                correctAnswers = newCorrect,
                xpEarned = newCorrect * 10
            )
        }
    }

    fun nextQuestion() {
        _uiState.update { state ->
            val nextIdx = state.currentQuestionIndex + 1
            if (nextIdx < state.totalQuestions) {
                state.copy(
                    currentQuestionIndex = nextIdx,
                    isChecked = false,
                    selectedOption = null,
                    placedTokenIndices = emptyList(),
                    selectedCharacterIndex = null,
                    selectedReadingIndex = null,
                    strokes = emptyList()
                )
            } else {
                state.copy(isFinished = true)
            }
        }
    }

    fun resetQuiz() {
        _uiState.value = QuizState()
    }
}
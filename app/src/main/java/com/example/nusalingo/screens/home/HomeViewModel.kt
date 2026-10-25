package com.example.nusalingo.screens.home

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class HomeViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(HomeState())
    val uiState: StateFlow<HomeState> = _uiState.asStateFlow()

    init {
        loadData()
    }

    private fun loadData() {
        _uiState.update { currentState ->
            currentState.copy(
                levelList = listOf(
                    LearningLevel(1, "Mengenal Aksara"),
                    LearningLevel(2, "Menggabungkan Aksara"),
                    LearningLevel(3, "Membangun Kalimat")
                ),
                aksaraList = listOf(
                    AksaraItem(id = "jawa", name = "Aksara Jawa", isAvailable = true, progressText = "3 to 3"),
                    AksaraItem(id = "madura", name = "Aksara Madura", isAvailable = false, progressText = "0 to 0"),
                    AksaraItem(id = "bali", name = "Aksara Bali", isAvailable = false, progressText = "0 to 0")
                )
            )
        }
    }

    fun selectAksara(aksara: AksaraItem) {
        if (aksara.isAvailable) {
            _uiState.update { it.copy(selectedAksaraName = aksara.name) }
        }
    }
}
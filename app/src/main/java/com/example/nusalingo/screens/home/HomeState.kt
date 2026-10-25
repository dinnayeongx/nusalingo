package com.example.nusalingo.screens.home

data class AksaraItem(
    val id: String,
    val name: String,
    val isAvailable: Boolean,
    val progressText: String
)

data class LearningLevel(
    val levelNumber: Int,
    val title: String
)

data class HomeState(
    val selectedAksaraName: String = "Aksara Jawa",
    val aksaraList: List<AksaraItem> = emptyList(),
    val levelList: List<LearningLevel> = emptyList(),
    val isLoading: Boolean = false
)

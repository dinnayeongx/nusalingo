package com.example.nusalingo.screens.lesson

data class ScriptChartGroup(
    val title: String,
    val rows: List<List<Pair<String, String>>>
)

data class LessonDetail(
    val title: String = "Carakan 1",
    val description: String = "Kenali sistem penulisan tradisional dan aksara utama dalam budaya Jawa",
    val chartSections: List<ScriptChartGroup> = emptyList()
)

data class LessonState(
    val lessonList: List<String> = emptyList(),
    val currentDetail: LessonDetail = LessonDetail(),
    val isLoading: Boolean = false
)
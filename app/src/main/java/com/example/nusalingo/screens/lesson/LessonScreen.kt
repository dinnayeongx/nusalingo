package com.example.nusalingo.screens.lesson

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ChevronRight
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.nusalingo.screens.home.BrandLogo
import com.example.nusalingo.ui.theme.NunitoFont
import com.example.nusalingo.ui.theme.NusalingoColors
import androidx.compose.runtime.getValue
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.viewmodel.compose.viewModel

private val lessonItems = listOf(
    "Carakan 1",
    "Carakan 2",
    "Carakan 3",
    "Sandhangan Swara",
    "Sandhangan Panyigeg Wanda",
    "Merangkai Kata"
)

@Composable
fun LessonScreen(
    onLessonSelected: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(NusalingoColors.Background)
            .padding(horizontal = 16.dp)
    ) {
        BrandLogo()

        Column(
            verticalArrangement = Arrangement.spacedBy(14.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            lessonItems.forEach { lesson ->
                LessonItemCard(
                    title = lesson,
                    onClick = {
                        onLessonSelected(lesson)
                    }
                )
            }
        }
    }
}

@Composable
fun LessonItemCard(
    title: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(55.dp)
            .clip(RoundedCornerShape(20.dp))
            .background(NusalingoColors.ActionPrimary)
            .clickable(onClick = onClick)
            .padding(
                start = 24.dp,
                end = 16.dp,
                top = 16.dp,
                bottom = 16.dp
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            modifier = Modifier.weight(1f),
            fontFamily = NunitoFont,
            fontSize = 20.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color.White,
            maxLines = 1
        )

        Icon(
            imageVector = Icons.Outlined.ChevronRight,
            contentDescription = "Buka $title",
            tint = Color.White
        )
    }
}

@Composable
fun LessonRoute(
    onLessonSelected: (String) -> Unit,
    viewModel: LessonViewModel = viewModel()
) {
    val state by viewModel.uiState.collectAsState()

    LessonScreen(
        state = state,
        onLessonSelected = { lessonTitle ->
            viewModel.selectLesson(lessonTitle)
            onLessonSelected(lessonTitle)
        }
    )
}

@Composable
fun LessonScreen(
    state: LessonState,
    onLessonSelected: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(NusalingoColors.Background)
            .padding(horizontal = 16.dp)
    ) {
        BrandLogo()

        Column(
            verticalArrangement = Arrangement.spacedBy(14.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            state.lessonList.forEach { lesson ->
                LessonItemCard(
                    title = lesson,
                    onClick = { onLessonSelected(lesson) }
                )
            }
        }
    }
}
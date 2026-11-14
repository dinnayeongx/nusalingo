package com.example.nusalingo.feature.lesson.screen

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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.nusalingo.feature.home.screen.BrandLogo
import com.example.nusalingo.feature.lesson.ui.LessonViewModel
import com.example.nusalingo.feature.lesson.ui.lessonTitles
import com.example.nusalingo.ui.theme.NunitoFont
import com.example.nusalingo.ui.theme.NusalingoColors

@Composable
fun LessonRoute(
    onLessonSelected: (String) -> Unit,
    viewModel: LessonViewModel = viewModel()
) {
    val completed by viewModel.completedLessons.collectAsState()

    LessonScreen(
        completedLessons = completed,
        onLessonSelected = onLessonSelected
    )
}

@Composable
fun LessonScreen(
    completedLessons: List<String>,
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
            lessonTitles.forEach { title ->
                LessonItemCard(
                    title = title,
                    isCompleted = title in completedLessons,
                    onClick = { onLessonSelected(title) }
                )
            }
        }
    }
}

@Composable
fun LessonItemCard(
    title: String,
    isCompleted: Boolean,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(55.dp)
            .clip(RoundedCornerShape(20.dp))
            .background(NusalingoColors.ActionPrimary)
            .clickable(onClick = onClick)
            .padding(start = 24.dp, end = 20.dp),
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
        Text(
            text = if (isCompleted) "✓" else "›",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )
    }
}
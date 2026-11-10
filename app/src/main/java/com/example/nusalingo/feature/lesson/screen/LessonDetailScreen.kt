package com.example.nusalingo.feature.lesson.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Book
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.nusalingo.feature.lesson.ui.LessonDetail
import com.example.nusalingo.feature.lesson.ui.LessonViewModel
import com.example.nusalingo.ui.theme.JavaneseFont
import com.example.nusalingo.ui.theme.NunitoFont
import com.example.nusalingo.ui.theme.NusalingoColors

@Composable
fun LessonDetailRoute(
    onBack: () -> Unit,
    onStartLesson: () -> Unit,
    viewModel: LessonViewModel = viewModel()
) {
    val state by viewModel.uiState.collectAsState()

    LessonDetailScreen(
        detail = state.currentDetail,
        onBack = onBack,
        onStartLesson = onStartLesson
    )
}

@Composable
fun LessonDetailScreen(
    detail: LessonDetail,
    onBack: () -> Unit,
    onStartLesson: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(
                    top = 16.dp,
                    bottom = 16.dp
                )
            ) {
                Text(
                    text = "‹",
                    modifier = Modifier
                        .clickable(onClick = onBack)
                        .padding(end = 18.dp),
                    fontSize = 44.sp,
                    color = Color.Black,
                    fontFamily = NunitoFont
                )

                Text(
                    text = detail.title,
                    fontFamily = NunitoFont,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 28.sp,
                    color = NusalingoColors.TextPrimary
                )
            }

            Text(
                text = detail.description,
                fontFamily = NunitoFont,
                fontSize = 16.sp,
                lineHeight = 25.sp,
                color = NusalingoColors.TextSecondary,
                modifier = Modifier.padding(
                    start = 8.dp,
                    bottom = 24.dp
                )
            )

            if (detail.isAvailable) {
                // Tombol mulai belajar hanya muncul jika materi tersedia
                Button(
                    onClick = onStartLesson,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(64.dp),
                    shape = RoundedCornerShape(22.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = NusalingoColors.ActionPrimary
                    )
                ) {
                    Text(
                        text = "Mulai Belajar",
                        fontFamily = NunitoFont,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.height(28.dp))

                detail.chartSections.forEach { chart ->
                    ScriptChartSection(
                        title = chart.title,
                        rows = chart.rows
                    )

                    Spacer(modifier = Modifier.height(24.dp))
                }
            } else {
                Spacer(modifier = Modifier.height(40.dp))

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(20.dp))
                        .background(Color.White)
                        .padding(
                            horizontal = 24.dp,
                            vertical = 36.dp
                        ),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Book,
                        contentDescription = "Materi belum tersedia",
                        tint = NusalingoColors.ActionSecondary,
                        modifier = Modifier.size(64.dp)
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    Text(
                        text = "Materi Belum Tersedia",
                        fontFamily = NunitoFont,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = NusalingoColors.TextPrimary
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = "Materi ${detail.title} sedang dalam pengembangan. Silakan pilih materi lain yang sudah tersedia.",
                        fontFamily = NunitoFont,
                        fontSize = 15.sp,
                        lineHeight = 23.sp,
                        color = NusalingoColors.TextSecondary
                    )
                }
            }
        }
    }
}

@Composable
fun ScriptChartSection(
    title: String,
    rows: List<List<Pair<String, String>>>
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(Color.White)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(NusalingoColors.ChartGreen)
                .padding(vertical = 8.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = title,
                color = Color.White,
                fontFamily = NunitoFont,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 16.sp
            )
        }

        Column(
            modifier = Modifier.padding(
                horizontal = 8.dp,
                vertical = 12.dp
            )
        ) {
            rows.forEach { row ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    row.forEach { (glyph, label) ->
                        Column(
                            modifier = Modifier
                                .weight(1f)
                                .padding(vertical = 8.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = glyph,
                                fontFamily = JavaneseFont,
                                fontSize = 28.sp,
                                color = Color.Black,
                                maxLines = 1
                            )

                            Text(
                                text = label,
                                fontFamily = NunitoFont,
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp,
                                color = Color(0xFF456B3C)
                            )
                        }
                    }
                }
            }
        }
    }
}
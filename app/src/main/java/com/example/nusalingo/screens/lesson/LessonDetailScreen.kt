package com.example.nusalingo.screens.lesson

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Book
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.PersonOutline
import androidx.compose.material.icons.outlined.ChevronRight
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.nusalingo.R
import com.example.nusalingo.screens.quiz.QuizFlowScreen
import com.example.nusalingo.screens.quiz.QuizType
import com.example.nusalingo.ui.theme.JavaneseFont
import com.example.nusalingo.ui.theme.NunitoFont
import com.example.nusalingo.ui.theme.NusalingoColors

@Composable
fun LessonDetailScreen(title: String = "Carakan 1", onBack: () -> Unit, onStartLesson: () -> Unit) {
    Column(Modifier.fillMaxSize()) {
        Column(Modifier.weight(1f).verticalScroll(rememberScrollState()).padding(horizontal = 24.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(top = 16.dp, bottom = 16.dp)) {
                Text("‹", modifier = Modifier.clickable(onClick = onBack).padding(end = 18.dp),
                    fontSize = 44.sp, color = Color.Black, fontFamily = NunitoFont)
                Text(title, fontFamily = NunitoFont, fontWeight = FontWeight.ExtraBold,
                    fontSize = 28.sp, color = NusalingoColors.TextPrimary)
            }
            Text("Kenali sistem penulisan tradisional dan aksara utama dalam budaya Jawa",
                fontFamily = NunitoFont, fontSize = 16.sp, lineHeight = 25.sp,
                color = NusalingoColors.TextSecondary, modifier = Modifier.padding(start = 8.dp, bottom = 24.dp))
            Button(
                onClick = onStartLesson,
                modifier = Modifier.fillMaxWidth().height(64.dp),
                shape = RoundedCornerShape(22.dp),
                colors = ButtonDefaults.buttonColors(containerColor = NusalingoColors.ActionPrimary)
            ) {
                Text("Mulai Belajar", fontFamily = NunitoFont, fontSize = 18.sp, fontWeight = FontWeight.Bold)
            }
            Spacer(Modifier.height(28.dp))
            ScriptChartSection(
                title = "AKSARA CARAKAN (nglegena)",
                rows = listOf(
                    listOf("ꦲ" to "ha", "ꦤ" to "na", "ꦕ" to "ca", "ꦫ" to "ra", "ꦏ" to "ka"),
                    listOf("ꦢ" to "da", "ꦠ" to "ta", "ꦱ" to "sa", "ꦮ" to "wa", "ꦭ" to "la"),
                    listOf("ꦥ" to "pa", "ꦝ" to "dha", "ꦗ" to "ja", "ꦪ" to "ya", "ꦚ" to "nya"),
                    listOf("ꦩ" to "ma", "ꦒ" to "ga", "ꦧ" to "ba", "ꦛ" to "tha", "ꦔ" to "nga")
                )
            )
            Spacer(Modifier.height(24.dp))
            ScriptChartSection(
                title = "AKSARA PASANGAN (mati)",
                rows = listOf(
                    listOf("꧀ꦲ" to "h", "꧀ꦤ" to "n", "꧀ꦕ" to "c", "꧀ꦫ" to "r", "꧀ꦏ" to "k"),
                    listOf("꧀ꦢ" to "d", "꧀ꦠ" to "t", "꧀ꦱ" to "s", "꧀ꦮ" to "w", "꧀ꦭ" to "l")
                )
            )
            Spacer(Modifier.height(24.dp))
        }
    }
}

@Composable
fun ScriptChartSection(title: String, rows: List<List<Pair<String, String>>>) {
    Column(Modifier.fillMaxWidth().clip(RoundedCornerShape(14.dp)).background(Color.White)) {
        Box(Modifier.fillMaxWidth().background(NusalingoColors.ChartGreen).padding(vertical = 8.dp), contentAlignment = Alignment.Center) {
            Text(title, color = Color.White, fontFamily = NunitoFont, fontWeight = FontWeight.ExtraBold, fontSize = 16.sp)
        }
        Column(Modifier.padding(horizontal = 8.dp, vertical = 12.dp)) {
            rows.forEach { row ->
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                    row.forEach { (glyph, label) ->
                        Column(Modifier.weight(1f).padding(vertical = 8.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(glyph, fontFamily = JavaneseFont,
                                fontSize = 28.sp, color = Color.Black, maxLines = 1)
                            Text(label, fontFamily = NunitoFont, fontWeight = FontWeight.Bold,
                                fontSize = 14.sp, color = Color(0xFF456B3C))
                        }
                    }
                }
            }
        }
    }
}

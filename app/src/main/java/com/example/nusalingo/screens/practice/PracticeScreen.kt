package com.example.nusalingo.screens.practice

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
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
import com.example.nusalingo.screens.home.BrandLogo
import com.example.nusalingo.screens.quiz.QuizFlowScreen
import com.example.nusalingo.screens.quiz.QuizType
import com.example.nusalingo.ui.theme.NunitoFont
import com.example.nusalingo.ui.theme.NusalingoColors

data class PracticeItem(val title: String, val type: QuizType)

@Composable
fun PracticeScreen(onPractice: (QuizType) -> Unit) {
    val items = listOf(
        PracticeItem("Pasangkan Aksara", QuizType.MATCHING_PAIRS),
        PracticeItem("Alih Aksara", QuizType.ALIH_AKSARA),
        PracticeItem("Gores Aksara", QuizType.WRITE_CHARACTER),
        PracticeItem("Rangkai Aksara", QuizType.RANGKAI_AKSARA),
        PracticeItem("Tebak Aksara", QuizType.MULTIPLE_CHOICE),
        PracticeItem("Uji Pendengaran", QuizType.AUDIO_QUIZ)
    )
    Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(horizontal = 20.dp)) {
        BrandLogo()
        items.forEachIndexed { index, item ->
            PracticeMenuItem(item.title) { onPractice(item.type) }
            if (index != items.lastIndex) Spacer(Modifier.height(16.dp))
        }
        Spacer(Modifier.height(12.dp))
    }
}

@Composable
fun PracticeMenuItem(title: String, onClick: () -> Unit) {
    Row(
        Modifier.fillMaxWidth().height(80.dp).clip(RoundedCornerShape(28.dp))
            .background(NusalingoColors.ActionPrimary).clickable(onClick = onClick)
            .padding(start = 20.dp, end = 18.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(R.drawable.mascot_face),
            contentDescription = "Maskot",
            modifier = Modifier.size(72.dp)
        )
        Spacer(Modifier.width(12.dp))
        Text(title, Modifier.weight(1f), fontFamily = NunitoFont, fontSize = 21.sp,
            lineHeight = 27.sp, fontWeight = FontWeight.Bold, color = Color.White,
            maxLines = 2, overflow = TextOverflow.Ellipsis)
        Icon(Icons.Outlined.ChevronRight, contentDescription = "Buka $title",
            tint = Color.White, modifier = Modifier.size(32.dp))
    }
}

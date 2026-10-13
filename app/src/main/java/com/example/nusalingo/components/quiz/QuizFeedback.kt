package com.example.nusalingo.components.quiz

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.nusalingo.ui.theme.NusalingoColors

@Composable
fun QuizFeedback(
    correct: Boolean,
    message: String,
    modifier: Modifier = Modifier
) {
    val background = if (correct) {
        NusalingoColors.GreenSecondary
    } else {
        Color(0xFFF4B6B6)
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(background)
            .padding(
                horizontal = 20.dp,
                vertical = 16.dp
            ),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Text(
            text = if (correct) "Kamu Berhasil!" else "Belum tepat!",
            fontWeight = FontWeight.ExtraBold,
            fontSize = 20.sp,
            color = NusalingoColors.TextPrimary
        )

        Text(
            text = message,
            fontSize = 14.sp,
            color = NusalingoColors.TextPrimary
        )
    }
}
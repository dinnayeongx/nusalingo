package com.example.nusalingo.components.quiz

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.nusalingo.ui.theme.NusalingoColors
import androidx.compose.ui.graphics.Color

enum class QuizAnswerState {
    DEFAULT, SELECTED, CORRECT, WRONG
}

@Composable
fun AnswerOption(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    state: QuizAnswerState = QuizAnswerState.DEFAULT
) {
    val background = when (state) {
        QuizAnswerState.DEFAULT -> Color.White
        QuizAnswerState.SELECTED -> Color(0xFFFFE0A3)
        QuizAnswerState.CORRECT -> NusalingoColors.GreenSecondary
        QuizAnswerState.WRONG -> Color(0xFFF4B6B6)
    }

    val border = when (state) {
        QuizAnswerState.DEFAULT -> NusalingoColors.InputBorder
        QuizAnswerState.SELECTED -> NusalingoColors.ActionSecondary
        QuizAnswerState.CORRECT -> NusalingoColors.GreenPrimary
        QuizAnswerState.WRONG -> Color(0xFFB3261E)
    }

    Surface(
        modifier = modifier
            .clickable { onClick() },
        shape = RoundedCornerShape(14.dp),
        color = background,
        border = BorderStroke(1.dp, border)
    ) {
        Box(
            modifier = Modifier.padding(
                horizontal = 32.dp,
                vertical = 14.dp
            ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = text,
                color = NusalingoColors.TextPrimary,
                fontSize = 16.sp,
                fontWeight = FontWeight.ExtraBold
            )
        }
    }
}
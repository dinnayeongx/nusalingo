package com.example.nusalingo.components.quiz

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.nusalingo.ui.theme.NusalingoColors

@Composable
fun MatchingPairCard(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    selected: Boolean = false,
    matched: Boolean = false,
    isCharacter: Boolean = false
) {
    val backgroundColor = when {
        matched -> NusalingoColors.GreenSecondary
        selected -> NusalingoColors.ActionPrimary
        else -> NusalingoColors.InputBackground
    }

    val textColor = when {
        selected && !matched -> NusalingoColors.TextInverse
        else -> NusalingoColors.TextPrimary
    }

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .height(92.dp)
            .clickable(enabled = !matched, onClick = onClick),
        shape = RoundedCornerShape(8.dp),
        color = backgroundColor,
        border = BorderStroke(
            width = 2.dp,
            color = if (selected) {
                NusalingoColors.ActionPrimary
            } else {
                NusalingoColors.InputBorder
            }
        )
    ) {
        Box(contentAlignment = Alignment.Center) {
            Text(
                text = text,
                color = textColor,
                fontSize = if (isCharacter) 32.sp else 24.sp,
                fontWeight = FontWeight.ExtraBold,
                textAlign = TextAlign.Center
            )
        }
    }
}